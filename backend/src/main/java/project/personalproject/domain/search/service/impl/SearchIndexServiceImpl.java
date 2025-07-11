package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.search.service.SearchIndexService;

@RequiredArgsConstructor
@Service
@Transactional
public class SearchIndexServiceImpl implements SearchIndexService {
    @Override
    public void indexPost(Post post) {

    }

    @Override
    public void updatePost(Post post) {

    }

    @Override
    public void deletePost(String postId) {

    }
}
