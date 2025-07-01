package project.personalproject.post.service;

import project.personalproject.post.dto.request.PostRequest;

public interface PostService {

    // 게시글 생성
    Long create (PostRequest postRequest);
}
