package project.personalproject.domain.post.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.exception.PostException;
import project.personalproject.global.exception.ErrorCode;

public interface PostRepository extends JpaRepository<Post, Long> {
    default Post getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostException(ErrorCode.NOT_FOUND_POST));
    }
}
