package project.personalproject.domain.post.image.service.impl;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.dto.PostImageDTO;
import project.personalproject.domain.post.image.dto.PostImageListDTO;
import project.personalproject.domain.post.image.dto.response.PostImageResponse;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.image.repository.PostImageRepository;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.repository.PostRepository;
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
    private final PostRepository postRepository;

    /**
     * 이미지 이름 프론트엔드에 반환
     */
    @Override
    public PostImageListDTO getPostImageByPostId(Long id, Pageable pageable) {
        Page<PostImage> postImages = postImageRepository.findByPostId(id, pageable);

        return PostImageListDTO.of(postImages);
    }

    /**
     * 게시글 이미지 생성
     * - 버킷이 없으면 새로 생성
     * - 이미지 파일을 MinIO에 업로드하고 presigned URL 반환
     */
    @Override
    public PostImageResponse createImages(Post post, List<MultipartFile> images) throws Exception {
        createBucketIfNotExists();

        List<String> imageNames = uploadToMinIO(images);
        savePostImages(post, imageNames);

        List<String> imageUrls = convertToUrls(imageNames);
        return PostImageResponse.of(imageUrls);

    }

    /**
     * 게시글 이미지 수정
     * - 기존 이미지 삭제 후 새로 업로드
     */
    @Override
    public PostImageResponse updateImages(Long postId, List<MultipartFile> images) throws Exception {
        deleteImages(postId);
        // 기존 post 조회해서 사용해야 함
        Post post = postRepository.findByIdOrThrow(postId);
        return createImages(post, images);
    }

    /**
     * 게시글 이미지 삭제
     * - postId로 등록된 이미지 파일명을 추출하여 MinIO에서 삭제
     */
    @Override
    public void deleteImages(Long postId) throws Exception {
        List<String> imageNames = getImageName(postId);

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
     * 실제 이미지 업로드 로직
     * - 파일명을 UUID로 생성
     * - 파일 스트림을 MinIO에 업로드
     */
    private List<String> uploadToMinIO(List<MultipartFile> images) throws Exception {
        List<String> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            String imageName = newImageName();

            // 리소스 누수 방지를 위해 try-with-resources 사용
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
     * 이미지 엔티티에 저장
     *
     * @param post
     * @param imageNames
     */
    private void savePostImages(Post post, List<String> imageNames) {
        for (String name : imageNames) {
            postImageRepository.save(PostImage.from(post, name));
        }
    }

    /**
     * 이미지 이름을 URL로 변환
     *
     * @param imageNames
     * @return
     * @throws Exception
     */
    private List<String> convertToUrls(List<String> imageNames) throws Exception {
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
     * 이미 저장되어 있는 이미지들을 불러옴
     */
    private List<String> getImageName(Long postId) {
        List<String> urls = postImageRepository.findUrlsByPostId(postId);

        List<String> imageNames = new ArrayList<>();

        for (String url : urls) {
            // URL에서 파일명 추출: ? 파라미터 제거
            String imageNameWithParams = url.substring(url.lastIndexOf("/") + 1);
            String imageName = imageNameWithParams.split("\\?")[0];
            imageNames.add(imageName);
        }

        return imageNames;
    }

    /**
     * UUID v1 기반으로 고유한 파일명을 생성
     * - "post-" 접두사 + UUID 문자열 (하이픈 제거)
     */
    private String newImageName() {
        TimeBasedGenerator generator = Generators.timeBasedGenerator();
        UUID uuid = generator.generate();
        return "post-" + uuid.toString().toLowerCase().replaceAll("-", "");
    }

    /**
     * final로 선언 시, minioProperties가 아직 초기화되기 전일 수 있으므로 위험
     * 안전하게 메서드로 분리해서 사용
     */
    private String getBucketName() {
        return minioProperties.getBucket().getName();
    }

    /**
     * 버킷이 존재하지 않으면 생성
     *
     * @throws Exception
     */
    private void createBucketIfNotExists() throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(getBucketName()).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(getBucketName()).build());
        }
    }

}
