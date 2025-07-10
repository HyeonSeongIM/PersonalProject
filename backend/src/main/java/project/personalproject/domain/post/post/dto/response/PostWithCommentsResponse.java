package project.personalproject.domain.post.post.dto.response;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;

public record PostWithCommentsResponse(
        PostResponse post,
        Page<PostCommentResponse> comments
) {
    public static PostWithCommentsResponse of(PostResponse post, Page<PostCommentResponse> comments) {
        return new PostWithCommentsResponse(post, comments);
    }
}
