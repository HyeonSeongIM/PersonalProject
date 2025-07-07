package project.personalproject.domain.post.post.service;

import project.personalproject.domain.post.post.dto.request.PostRequest;
import project.personalproject.domain.post.post.dto.response.PostResponse;

public interface PostService {

    // 게시글 생성
    PostResponse createPost (PostRequest postRequest);
}
