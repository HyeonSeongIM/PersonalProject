package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.repository.SearchRepository;
import project.personalproject.domain.search.service.SearchService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public List<Search> searchByKeyword(String keyword) {

        List<Search> results = searchRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        return List.of();
    }

    @Override
    public List<Search> searchByFilter(PostCategory category, PostTag tag, int page, int size) {
        return List.of();
    }
}
