package project.personalproject.domain.post.post.dto.request;

public record UpdatePostCommand(
        String title,
        String content
) {
}
