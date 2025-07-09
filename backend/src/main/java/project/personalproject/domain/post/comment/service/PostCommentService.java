package project.personalproject.domain.post.comment.service;

import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.dto.request.CreateCommentCommand;
import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;

import java.util.List;

public interface PostCommentService {

    // 댓글 생성
    PostCommentResponse createComment(CreateCommentCommand command, Member member);

    // 댓글 삭제
    void deleteComment(Long commentId, Member member);

    // 전체 댓글 조회
    List<PostCommentResponse> getCommentByPost(Long postId);

}
