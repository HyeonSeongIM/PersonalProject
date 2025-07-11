package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.dto.response.SearchResponse;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.repository.SearchRepository;
import project.personalproject.domain.search.service.SearchService;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public Page<SearchResponse> searchByKeyword(String keyword, Pageable pageable) {

        Page<Search> results = searchRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        return SearchResponse.pageOf(results);
    }

    @Override
    public Page<SearchResponse> searchByFilter(PostCategory category, PostTag tag, Pageable pageable) {

        Page<Search> results = searchRepository.findByCategoryAndTag(category.name(), tag.name(), pageable);

        return SearchResponse.pageOf(results);
    }
}
