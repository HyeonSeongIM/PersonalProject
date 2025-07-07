package project.personalproject.domain.post.post.dto.response;

public record PostResponse(
        String title,
        String content,
        String memberName,
        String createDate
) {
}
