package project.personalproject.domain.post.post.dto.response;

import project.personalproject.domain.post.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostResponse(
        String title,
        String content,
        String memberName,
        LocalDateTime createDate
) {
    /**
     * Data -> DTO 변환 로직
     * @param post
     * @return
     */
    public static PostResponse of(Post post) {
        return new PostResponse(
                post.getTitle(),
                post.getContent(),
                post.getMember().getUsername(),
                post.getCreateDate()
        );
    }

    /**
     * 2개 이상의 Data -> 2개 이상의 DTO
     * @param posts
     * @return
     */
    public static List<PostResponse> listOf(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }
}
