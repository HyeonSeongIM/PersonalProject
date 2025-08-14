package project.personalproject.domain.post.image.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.post.image.dto.PostImageListDTO;
import project.personalproject.domain.post.image.dto.response.PostImageResponse;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.image.exception.PostImageException;
import project.personalproject.domain.post.image.repository.PostImageRepository;
import project.personalproject.domain.post.image.service.PostImageIoService;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.global.exception.ErrorCode;

import java.util.List;

/**
 * 게시글 이미지 오케스트레이션 서비스.
 * - 외부 IO(MinIO 업로드/삭제/URL 생성)는 PostImageIoService(NOT_SUPPORTED)로 분리
 * - DB 저장/삭제는 여기서 수행 (상위 트랜잭션에 참여하거나, 호출부에서 @Transactional로 감싸는 구조)
 */
@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final PostImageIoService postImageIoService;

    /**
     * 게시글 ID로 PostImage 페이지를 조회.
     *
     * @param id        게시글 ID
     * @param pageable  페이지 정보
     * @return          PostImageListDTO (페이지 래핑)
     */
    @Override
    public PostImageListDTO getPostImageByPostId(Long id, Pageable pageable) {
        Page<PostImage> postImages = postImageRepository.findByPostId(id, pageable);
        return PostImageListDTO.of(postImages);
    }

    /**
     * 게시글 이미지 생성.
     * 흐름: 파일 수 검증(비TX) → 업로드(NOT_SUPPORTED) → DB 저장(상위 TX 참여) → presigned URL 반환(NOT_SUPPORTED)
     *
     * @param post   이미지가 연결될 게시글(영속 상태 권장)
     * @param images 업로드할 멀티파트 이미지들
     * @return       presigned URL 목록을 담은 응답 DTO
     * @throws Exception MinIO/네트워크 예외 등
     *
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PostImageResponse createImages(Post post, List<MultipartFile> images) throws Exception {
        fileCountCheck(images); // 단순 검증

        List<String> imageNames = postImageIoService.uploadToMinIO(images); // 비TX(IO)

        saveImageOrThrow(post, imageNames); // DB 저장 (상위 TX에 참여한다는 가정)

        return PostImageResponse.of(postImageIoService.convertToUrls(imageNames)); // 비TX(IO)
    }

    /**
     * 게시글 이미지 수정.
     * 단순화한 흐름: 기존 삭제 → 신규 업로드/저장.
     * 필요 시 "신규 업로드 → DB 교체 → 구 객체 삭제" 순서로 바꾸면 더 안전하다.
     *
     * @param postId 게시글 ID
     * @param images 신규 이미지 목록
     * @return       presigned URL 목록 응답
     * @throws Exception IO/DB 관련 예외
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PostImageResponse updateImages(Long postId, List<MultipartFile> images) throws Exception {
        deleteImages(postId); // DB→IO 순으로 정리

        Post post = postRepository.findByIdOrThrow(postId);
        return createImages(post, images);
    }

    /**
     * 게시글 이미지 삭제.
     * 흐름: (DB에서 이름 조회) → DB 삭제(TX) → MinIO 객체 삭제(비TX)
     * DB를 먼저 지우는 이유는, IO 성공 후 DB가 실패할 경우 정합성 깨짐을 방지하기 위함.
     *
     * @param postId 게시글 ID
     * @throws Exception MinIO 예외 등
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteImages(Long postId) throws Exception {
        List<String> names = postImageRepository.findNamesByPostId(postId); // DB 조회
        postImageRepository.deleteAllByPostId(postId);                      // DB 삭제(TX)
        postImageIoService.deleteImages(names);                             // MinIO 삭제(비TX)
    }

    /**
     * 이미지 엔티티 저장(예외 시 업로드 보상 삭제).
     *
     * @param post        연결할 게시글
     * @param imageNames  MinIO에 업로드된 객체명 목록
     * @throws Exception  보상 삭제 실패 등
     *
     */
    public void saveImageOrThrow(Post post, List<String> imageNames) throws Exception {
        try {
            savePostImages(post, imageNames);
        } catch (RuntimeException e) {
            // DB 저장 실패 시 업로드 보상 삭제
            postImageIoService.deleteImages(imageNames);
            throw new PostImageException(ErrorCode.NOT_UPLOAD_IMAGE);
        }
    }

    /**
     * 이미지 엔티티 저장 (배치 저장).
     *
     * @param post        연결할 게시글
     * @param imageNames  객체명 목록
     */
    public void savePostImages(Post post, List<String> imageNames) {
        var entities = imageNames.stream()
                .map(n -> PostImage.from(post, n))
                .toList();
        postImageRepository.saveAll(entities);
    }

    /**
     * 파일 개수 검증(최대 5개).
     *
     * @param images 업로드할 파일 목록
     * @throws PostImageException TOO_MANY_FILES
     */
    private void fileCountCheck(List<MultipartFile> images) {
        if (images.size() > 5) {
            throw new PostImageException(ErrorCode.TOO_MANY_FILES);
        }
    }

    /**
     * 게시글에 연결된 이미지 이름 목록 조회(편의 메서드).
     *
     * @param postId 게시글 ID
     * @return       이미지 객체명 리스트
     */
    private List<String> getImageName(Long postId) {
        return postImageRepository.findNamesByPostId(postId);
    }
}
