package project.personalproject.domain.post.post.dto.response;

import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;

import java.util.List;

public record PostWithCommentsResponse(
        PostResponse post,
        List<PostCommentResponse> comments
) {
    public static PostWithCommentsResponse of(PostResponse post, List<PostCommentResponse> comments) {
        return new PostWithCommentsResponse(post, comments);
    }
}
