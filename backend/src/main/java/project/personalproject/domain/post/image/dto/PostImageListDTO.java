package project.personalproject.domain.post.image.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PostImageListDTO(
        List<PostImageDTO> images,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public static PostImageListDTO of(Page<PostImageDTO> images) {
        return new PostImageListDTO(
                images.getContent(),
                images.getTotalPages(),
                images.getTotalElements(),
                images.getNumber(),
                images.getSize()
        );
    }
}
