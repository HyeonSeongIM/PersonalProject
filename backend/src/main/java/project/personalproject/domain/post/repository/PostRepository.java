package project.personalproject.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
