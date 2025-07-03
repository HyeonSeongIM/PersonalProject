package project.personalproject.domain.post.service;

import project.personalproject.domain.post.dto.request.PostRequest;

public interface PostService {

    // 게시글 생성
    Long create (PostRequest postRequest);
}
