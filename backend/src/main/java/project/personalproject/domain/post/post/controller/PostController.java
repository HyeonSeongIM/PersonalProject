package project.personalproject.domain.post.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.dto.request.CreateCommentCommand;
import project.personalproject.domain.post.comment.dto.request.DeleteCommentCommand;
import project.personalproject.domain.post.comment.dto.response.PostCommentResponse;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.dto.response.PostWithCommentsResponse;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.global.security.jwt.JwtService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final PostCommentService postCommentService;
    private final JwtService jwtService;

    /**
     * 게시글 목록 조회 (페이징)
     *
     * @param pageable 페이징 정보
     * @return 게시글 리스트 (페이지 형태)
     */
    @GetMapping("/list")
    public ResponseEntity<Page<PostResponse>> getPostList(Pageable pageable) {
        return ResponseEntity.ok(postService.getPostList(pageable));
    }

    /**
     * 단일 게시글 상세 조회
     *
     * @param postId 게시글 ID
     * @return 게시글 응답 DTO
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostWithCommentsResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostWithComments(postId));
    }

    /**
     * 게시글 생성
     *
     * @param postRequest 게시글 생성 요청 DTO
     * @param images      이미지 파일 리스트
     * @param request     Http 요청 객체 (토큰에서 사용자 정보 추출용)
     * @return 생성된 게시글 응답 DTO
     * @throws Exception 파일 저장 실패 등 예외 발생 시
     */
    @PostMapping("/create")
    public ResponseEntity<PostResponse> postCreate(@RequestPart("postRequest") CreatePostCommand postRequest,
                                                   @RequestPart("images") List<MultipartFile> images,
                                                   HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(postService.createPost(postRequest, getMemberFromToken(request), images));
    }

    /**
     * 게시글 수정
     *
     * @param postRequest 수정 요청 DTO
     * @param images      이미지 파일 리스트
     * @param postId      게시글 ID
     * @param request     Http 요청 객체
     * @return 수정된 게시글 응답 DTO
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> postUpdate(@RequestPart("postRequest") UpdatePostCommand postRequest,
                                                   @RequestPart("images") List<MultipartFile> images,
                                                   @PathVariable("postId") Long postId,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, postRequest, getMemberFromToken(request), images));
    }

    /**
     * 게시글 삭제
     *
     * @param postId  삭제할 게시글 ID
     * @param request Http 요청 객체
     * @return 삭제된 게시글 응답 DTO
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> postDelete(@PathVariable("postId") Long postId,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.deletePost(postId, getMemberFromToken(request)));
    }

    /**
     * 댓글 생성
     *
     * @param commentRequest 댓글 생성 요청 DTO
     * @param request        Http 요청 객체
     * @return 생성된 댓글 응답 DTO
     */
    @PostMapping("/comment")
    public ResponseEntity<PostCommentResponse> createComment(@RequestBody CreateCommentCommand commentRequest,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok(postCommentService.createComment(commentRequest, getMemberFromToken(request)));
    }

    /**
     * 댓글 삭제
     *
     * @param commentRequest 삭제할 댓글 ID 포함된 요청 DTO
     * @param request        Http 요청 객체
     */
    @DeleteMapping("/comment")
    public void deleteComment(@RequestBody DeleteCommentCommand commentRequest,
                              HttpServletRequest request) {
        postCommentService.deleteComment(commentRequest.commentId(), getMemberFromToken(request));
    }

    /**
     * 토큰에서 Member 정보 추출
     *
     * @param request Http 요청 객체
     * @return 토큰에서 추출한 Member 엔티티
     */
    private Member getMemberFromToken(HttpServletRequest request) {
        return jwtService.getMemberFromToken(request);
    }
}
