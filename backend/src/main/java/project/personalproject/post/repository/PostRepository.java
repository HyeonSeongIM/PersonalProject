package project.personalproject.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
