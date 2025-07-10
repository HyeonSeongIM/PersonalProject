package project.personalproject.domain.post.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.global.exception.ErrorCode;

/**
 * 게시글(Post) 엔티티용 Repository
 * - 기본 CRUD는 JpaRepository를 통해 상속
 * - 도메인 특화 예외 처리를 위한 헬퍼 메서드 포함
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * ID로 게시글 조회
     * - 존재하지 않을 경우 PostException 발생
     *
     * @param id 게시글 ID
     * @return 조회된 게시글
     * @throws PostException 게시글이 존재하지 않을 경우
     */
    default Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));
    }

    /**
     * 페이징된 게시글 목록 조회
     * - 결과가 비어 있을 경우 PostException 발생
     *
     * @param pageable 페이징 정보
     * @return 페이징된 게시글 목록
     * @throws PostException 게시글이 하나도 없을 경우
     */
    default Page<Post> findAllOrThrow(Pageable pageable) {
        Page<Post> page = findAll(pageable);
        if (page.isEmpty()) {
            throw new PostException(ErrorCode.NOT_FOUND_POST_ALL);
        }
        return page;
    }
}
