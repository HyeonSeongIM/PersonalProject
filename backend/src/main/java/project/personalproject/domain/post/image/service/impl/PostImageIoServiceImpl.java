package project.personalproject.domain.post.image.service.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.service.PostImageIoService;
import project.personalproject.global.miniO.MinioProperties;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 이미지 IO 전담 서비스.
 * 클래스 레벨 @Transactional(NOT_SUPPORTED)로 모든 퍼블릭 메서드는 비트랜잭션으로 실행된다.
 *
 * - 여기서는 JPA/Repository 접근 금지 (비TX 구간)
 * - 순수 MinIO/S3 네트워크 IO와 URL 생성만 담당
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class PostImageIoServiceImpl implements PostImageIoService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * 이미지 업로드.
     * - 버킷이 없으면 생성
     * - 파일명을 UUID(v1)로 생성
     *
     * @param images 업로드할 멀티파트 이미지
     * @return       업로드된 객체명 리스트
     * @throws Exception MinIO 예외
     */
    @Override
    public List<String> uploadToMinIO(List<MultipartFile> images) throws Exception {
        createBucketIfNotExists();

        List<String> imageList = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageName = newImageName();

            // InputStream 자원 누수 방지
            try (InputStream imageStream = image.getInputStream()) {
                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(getBucketName())
                        .object(imageName)
                        .stream(imageStream, image.getSize(), -1)
                        .contentType(image.getContentType())
                        .build();

                minioClient.putObject(putObjectArgs);
            }

            imageList.add(imageName);
        }
        return imageList;
    }

    /**
     * 이미지 객체 삭제.
     *
     * @param imageNames 삭제할 객체명 리스트
     * @throws Exception MinIO 예외
     */
    @Override
    public void deleteImages(List<String> imageNames) throws Exception {
        for (String image : imageNames) {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(getBucketName())
                            .object(image)
                            .build()
            );
        }
    }

    /**
     * presigned URL 생성.
     *
     * @param imageNames 객체명 리스트
     * @return           presigned URL 리스트
     * @throws Exception MinIO 예외
     */
    @Override
    public List<String> convertToUrls(List<String> imageNames) throws Exception {
        List<String> urls = new ArrayList<>();
        for (String name : imageNames) {
            urls.add(minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(getBucketName())
                            .object(name)
                            .build()));
        }
        return urls;
    }

    /**
     * 버킷이 없으면 생성.
     *
     * @throws Exception MinIO 예외
     */
    public void createBucketIfNotExists() throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(getBucketName()).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(getBucketName()).build());
        }
    }

    /**
     * UUID v1 기반 새 객체명 생성.
     * v1(UUID) 사용 이유: 시간 기반으로 충돌 가능성↓, 정렬/탐색에도 유리
     * @return "post-" 접두사의 고유 객체명
     */
    public String newImageName() {
        TimeBasedGenerator generator = Generators.timeBasedGenerator();
        UUID uuid = generator.generate();
        return "post-" + uuid.toString().toLowerCase().replaceAll("-", "");
    }

    /**
     * 버킷명 조회(설정 값).
     *
     * @return 버킷명
     */
    public String getBucketName() {
        return minioProperties.getBucket().getName();
    }
}
