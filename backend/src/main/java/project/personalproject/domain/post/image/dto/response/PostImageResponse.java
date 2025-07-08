package project.personalproject.domain.post.image.dto.response;

import java.util.List;

public record PostImageResponse(
        List<String> imageUrls
) {
    public static PostImageResponse of(List<String> imageUrls) {
        return new PostImageResponse(imageUrls);
    }
}
