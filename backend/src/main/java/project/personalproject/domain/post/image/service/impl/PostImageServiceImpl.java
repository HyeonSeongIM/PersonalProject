package project.personalproject.domain.post.image.service.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.dto.response.PostImageResponse;
import project.personalproject.domain.post.image.repository.PostImageRepository;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.global.miniO.MinioProperties;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final PostImageRepository postImageRepository;

    /**
     * 게시글 이미지 생성
     * - 버킷이 없으면 새로 생성
     * - 이미지 파일을 MinIO에 업로드하고 presigned URL 반환
     */
    @Override
    public PostImageResponse createImages(List<MultipartFile> files) throws Exception {
        // 버킷 존재 여부 확인 후 없으면 생성
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(getBucketName()).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(getBucketName()).build());
        }

        return uploadImage(files);
    }

    /**
     * 게시글 이미지 수정
     * - 기존 이미지 삭제 후 새로 업로드
     */
    @Override
    public PostImageResponse updateImages(Long postId, List<MultipartFile> files) throws Exception {
        deleteImages(postId);
        return uploadImage(files);
    }

    /**
     * 게시글 이미지 삭제
     * - postId로 등록된 이미지 파일명을 추출하여 MinIO에서 삭제
     */
    @Override
    public void deleteImages(Long postId) throws Exception {
        List<String> fileNames = getFileName(postId);

        for (String file : fileNames) {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(getBucketName())
                            .object(file)
                            .build()
            );
        }
    }

    /**
     * 실제 이미지 업로드 로직
     * - 파일명을 UUID로 생성
     * - 파일 스트림을 MinIO에 업로드
     * - presigned URL을 생성하여 응답 DTO에 담아 반환
     */
    private PostImageResponse uploadImage(List<MultipartFile> files) throws Exception {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = newFileName();

            // 리소스 누수 방지를 위해 try-with-resources 사용
            try (InputStream fileStream = file.getInputStream()) {
                PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                        .bucket(getBucketName())
                        .object(fileName)
                        .stream(fileStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build();

                minioClient.putObject(putObjectArgs);
            }

            // presigned URL 생성
            String fileUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(getBucketName())
                            .object(fileName)
                            .build()
            );

            urls.add(fileUrl);
        }

        return PostImageResponse.of(urls);
    }

    /**
     * postId를 기반으로 저장된 presigned URL 리스트를 조회한 뒤
     * 그 안에서 실제 파일명을 파싱하여 반환
     */
    private List<String> getFileName(Long postId) {
        List<String> urls = postImageRepository.findUrlsByPostId(postId);
        List<String> fileNames = new ArrayList<>();

        for (String url : urls) {
            // URL에서 파일명 추출: ? 파라미터 제거
            String fileNameWithParams = url.substring(url.lastIndexOf("/") + 1);
            String fileName = fileNameWithParams.split("\\?")[0];
            fileNames.add(fileName);
        }

        return fileNames;
    }

    /**
     * UUID v1 기반으로 고유한 파일명을 생성
     * - "post-" 접두사 + UUID 문자열 (하이픈 제거)
     */
    private String newFileName() {
        TimeBasedGenerator generator = Generators.timeBasedGenerator();
        UUID uuid = generator.generate();
        return "post-" + uuid.toString().toLowerCase().replaceAll("-", "");
    }

    // ❌ final로 선언 시, minioProperties가 아직 초기화되기 전일 수 있으므로 위험
    // 안전하게 메서드로 분리해서 사용
    private String getBucketName() {
        return minioProperties.getBucket().getName();
    }

}
