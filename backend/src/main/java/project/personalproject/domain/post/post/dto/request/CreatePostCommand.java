package project.personalproject.domain.post.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostCommand(
        @NotBlank @Size(min = 1, max = 30)
        String title,

        @NotBlank @Size(min = 1)
        String content
) {

}
