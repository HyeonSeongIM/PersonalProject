package project.personalproject.domain.search.service;

import project.personalproject.domain.post.post.entity.Post;

public interface SearchIndexService {
    void indexPost(Post post);          // 인덱싱

    void updatePost(Post post);         // 수정

    void deletePost(String postId);     // 삭제
}
