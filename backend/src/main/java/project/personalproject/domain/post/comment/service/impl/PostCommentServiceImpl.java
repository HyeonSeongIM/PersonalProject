package project.personalproject.domain.post.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.dto.PostCommentListDTO;
import project.personalproject.domain.post.comment.dto.request.CreateCommentCommand;
import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.comment.exception.PostCommentException;
import project.personalproject.domain.post.comment.repository.PostCommentRepository;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentServiceImpl implements PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성
     *
     * @param command 댓글 생성 요청 DTO
     * @param member  현재 로그인한 사용자
     * @return 생성된 댓글 응답 DTO
     */
    @Override
    public PostCommentResponse createComment(CreateCommentCommand command, Member member) {
        PostComment postComment = PostComment.from(command, member, getPost(command.postId()));
        postCommentRepository.save(postComment);
        return PostCommentResponse.of(postComment);
    }

    /**
     * 댓글 삭제 (작성자 본인만 가능)
     *
     * @param commentId 삭제할 댓글 ID
     * @param member    현재 로그인한 사용자
     */
    @Override
    public void deleteComment(Long commentId, Member member) {
        PostComment postComment = getPostCommentIfSameUser(commentId, member);
        postCommentRepository.delete(postComment);
    }

    // TODO: 이거 제네릭 타입 일치시켜야함

    /**
     * 게시글에 달린 댓글 전체 조회
     *
     * @param postId 게시글 ID
     * @return 댓글 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    @Override
    public PostCommentListDTO getCommentByPostId(Long postId, Pageable pageable) {

        Page<PostComment> comments = postCommentRepository.findByPostId(postId, pageable);

        return PostCommentListDTO.of(comments);
    }

    /**
     * 게시글 조회 (없으면 예외 발생)
     *
     * @param postId 게시글 ID
     * @return 게시글 엔티티
     */
    private Post getPost(Long postId) {
        return postRepository.findByIdOrThrow(postId);
    }

    /**
     * 댓글 작성자인지 검증 (아닐 경우 예외 발생)
     *
     * @param commentId 댓글 ID
     * @param member    현재 로그인한 사용자
     * @return 댓글 엔티티
     */
    private PostComment getPostCommentIfSameUser(Long commentId, Member member) {
        PostComment comment = postCommentRepository.findByIdOrThrow(commentId);
        if (!member.getId().equals(comment.getMember().getId())) {
            throw new PostCommentException(ErrorCode.NOT_MATCH_USER);
        }
        return comment;
    }
}
