package project.personalproject.domain.post.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.image.entity.PostImage;

public interface PostCommentRepository extends JpaRepository<PostImage, Long> {
}
