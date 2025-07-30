package project.personalproject.domain.post.image.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.personalproject.domain.post.image.entity.PostImage;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Query("select pi.imageName from PostImage pi where pi.post.id = :postId")
    List<String> findUrlsByPostId(@Param("postId") Long postId);

    Page<PostImage> findByPostId(Long postId, Pageable pageable);
}
