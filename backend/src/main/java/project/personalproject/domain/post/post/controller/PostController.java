package project.personalproject.domain.post.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
import project.personalproject.domain.post.post.dto.response.PostResponse;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.global.security.jwt.JwtService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<PostResponse> postCreate(@RequestBody CreatePostCommand postRequest,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.createPost(postRequest, getMemberFromToken(request)));
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<PostResponse> postUpdate(@RequestBody UpdatePostCommand postRequest,
                                                   @PathVariable("postId") Long postId,
                                                   HttpServletRequest request) {
        return ResponseEntity.ok(postService.updatePost(
                postId,
                postRequest,
                getMemberFromToken(request))
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
