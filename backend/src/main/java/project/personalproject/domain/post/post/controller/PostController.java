package project.personalproject.domain.post.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.global.security.jwt.JwtService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    @GetMapping("/list")
    public ResponseEntity<Page<PostResponse>> getPostList(Pageable pageable) {
        return ResponseEntity.ok(postService.getPostList(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping("/create")
    public ResponseEntity<PostResponse> postCreate(@RequestPart("postRequest") CreatePostCommand postRequest,
                                                   @RequestPart("images") List<MultipartFile> images,
                                                   HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(postService.createPost(postRequest, getMemberFromToken(request), images));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> postUpdate(@RequestPart("postRequest") UpdatePostCommand postRequest,
                                                   @RequestPart("images") List<MultipartFile> images,
                                                   @PathVariable("postId") Long postId,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, postRequest, getMemberFromToken(request), images)
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> postDelete(@PathVariable("postId") Long postId,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.deletePost(postId, getMemberFromToken(request)));
    }

    private Member getMemberFromToken(HttpServletRequest request) {
        return jwtService.getMemberFromToken(request);
    }
}
