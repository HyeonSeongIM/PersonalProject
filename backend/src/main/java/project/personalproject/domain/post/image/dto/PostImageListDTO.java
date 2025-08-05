package project.personalproject.domain.post.image.dto;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.image.entity.PostImage;

import java.util.List;

public record PostImageListDTO(
        List<PostImage> images,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public static PostImageListDTO of(Page<PostImage> images) {
        return new PostImageListDTO(
                images.getContent(),
                images.getTotalPages(),
                images.getTotalElements(),
                images.getNumber(),
                images.getSize()
        );
    }
}
