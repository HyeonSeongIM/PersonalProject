package project.personalproject.domain.post.post.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PostListDTO(
        List<PostDTO> posts,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public static PostListDTO of(Page<PostDTO> posts) {
        return new PostListDTO(
                posts.getContent(),
                posts.getTotalPages(),
                posts.getTotalElements(),
                posts.getNumber(),
                posts.getSize()
        );
    }
}
