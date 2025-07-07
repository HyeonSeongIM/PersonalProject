package project.personalproject.domain.post.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
