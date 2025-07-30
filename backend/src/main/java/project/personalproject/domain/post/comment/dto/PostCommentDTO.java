package project.personalproject.domain.post.comment.dto;

import org.springframework.data.domain.Page;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;

import java.util.List;
import java.util.stream.Collectors;

public record PostCommentDTO(
        Long id,
        String comment,
        Member member
) {

    public static PostCommentDTO of (PostComment postComment) {
        return new PostCommentDTO(
                postComment.getId(),
                postComment.getComment(),
                postComment.getMember()
        );
    }
    public static List<PostCommentDTO> listOf(Page<PostComment> comments) {
        return comments.stream()
                .map(PostCommentDTO::of)
                .collect(Collectors.toList());
    }
}
