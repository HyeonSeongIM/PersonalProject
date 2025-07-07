package project.personalproject.domain.post.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.comment.entity.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
