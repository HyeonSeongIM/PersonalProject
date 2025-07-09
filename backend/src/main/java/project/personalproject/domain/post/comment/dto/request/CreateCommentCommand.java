package project.personalproject.domain.post.comment.dto.request;

public record CreateCommentCommand(
        Long postId,
        String comment
) {
}
