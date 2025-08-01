package project.personalproject.domain.post.comment.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PostCommentListDTO(
        List<PostCommentDTO> postComments,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public static PostCommentListDTO of(Page<PostCommentDTO> comments) {
        return new PostCommentListDTO(
                comments.getContent(),
                comments.getTotalPages(),
                comments.getNumberOfElements(),
                comments.getNumber(),
                comments.getSize()
        );
    }
}
