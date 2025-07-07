package project.personalproject.domain.post.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.post.post.dto.request.PostRequest;
import project.personalproject.domain.post.post.service.PostService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    // TODO : 게시글 생성 API 구성
    @PostMapping("/create")
    public ResponseEntity<?> postCreate(@RequestBody PostRequest postRequest) {

        Long postId = postService.create(postRequest);

        Map<String, Object> response = Map.of("id", postId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
