package project.personalproject.domain.post.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.comment.exception.PostCommentException;
import project.personalproject.global.exception.ErrorCode;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    default PostComment findByIdOrThrow(Long postId) {
        return findById(postId).orElseThrow(() -> new PostCommentException(ErrorCode.NOT_FOUND_COMMENT));
    }

    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
