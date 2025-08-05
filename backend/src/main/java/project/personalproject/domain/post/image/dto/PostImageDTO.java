package project.personalproject.domain.post.image.dto;

import project.personalproject.domain.post.image.entity.PostImage;

import java.util.List;
import java.util.stream.Collectors;

public record PostImageDTO(
        Long id,
        String imageName
) {
    public static PostImageDTO of(PostImage postImage) {
        return new PostImageDTO(
                postImage.getId(),
                postImage.getImageName());
    }

    public static List<PostImageDTO> listOf(List<PostImage> postImages) {
        return postImages.stream()
                .map(PostImageDTO::of)
                .collect(Collectors.toList());
    }
}
