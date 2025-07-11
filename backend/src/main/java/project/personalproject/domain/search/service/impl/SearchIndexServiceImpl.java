package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.repository.SearchRepository;
import project.personalproject.domain.search.service.SearchIndexService;

@RequiredArgsConstructor
@Service
public class SearchIndexServiceImpl implements SearchIndexService {

    private final SearchRepository searchRepository;

    @Override
    public void indexPost(Post post) {
        Search index = Search.from(post);

        searchRepository.save(index);
    }

    @Override
    public void updatePost(Post post) {
        Search index = Search.from(post);

        searchRepository.save(index);
    }

    @Override
    public void deletePost(Long postId) {
        searchRepository.deleteById(postId);
    }
}
