package project.personalproject.domain.post.comment.dto.response;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.comment.entity.PostComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostCommentResponse(
        Long id,
        String username,
        String comment,
        LocalDateTime createdDate
) {

    public static PostCommentResponse of(PostComment postComment) {
        return new PostCommentResponse(
                postComment.getId(),
                postComment.getMember().getUsername(),
                postComment.getComment(),
                postComment.getCreateDate()
        );
    }

    public static List<PostCommentResponse> listOf(List<PostComment> postComments) {
        return postComments.stream()
                .map(PostCommentResponse::of)
                .collect(Collectors.toList());
    }

    public static Page<PostCommentResponse> pageOf(Page<PostComment> postComments) {
        return postComments.map(PostCommentResponse::of);
    }
}
