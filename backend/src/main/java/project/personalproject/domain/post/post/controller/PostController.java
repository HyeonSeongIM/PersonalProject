package project.personalproject.domain.post.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
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
        Member member = jwtService.getMemberFromToken(request);
        return ResponseEntity.ok(postService.createPost(postRequest, member));
    }
}
