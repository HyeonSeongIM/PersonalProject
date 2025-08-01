package project.personalproject.domain.post.comment.dto;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.comment.entity.PostComment;

import java.util.List;

public record PostCommentListDTO(
        List<PostComment> postComments,
        int totalPages,
        long totalElements,
        int number,
        int size
) {
    public static PostCommentListDTO of(Page<PostComment> comments) {
        return new PostCommentListDTO(
                comments.getContent(),
                comments.getTotalPages(),
                comments.getNumberOfElements(),
                comments.getNumber(),
                comments.getSize()
        );
    }
}
