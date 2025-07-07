package project.personalproject.domain.post.post.dto.request;

public record CreatePostCommand(
        String title,
        String content
) {

}
