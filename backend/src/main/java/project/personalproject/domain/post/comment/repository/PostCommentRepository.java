package project.personalproject.domain.post.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.comment.exception.PostCommentException;
import project.personalproject.global.exception.ErrorCode;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    default PostComment findByIdOrThrow(Long postId) {
        return findById(postId).orElseThrow(() -> new PostCommentException(ErrorCode.NOT_FOUND_COMMENT));
    }

    List<PostComment> findByPostId(Long postId);
}
