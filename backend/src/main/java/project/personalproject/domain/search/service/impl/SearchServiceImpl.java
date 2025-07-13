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

    /**
     * 키워드로 게시글 검색 (title 또는 content에 포함된 경우)
     *
     * @param keyword  검색 키워드
     * @param pageable 페이징 정보
     * @return 페이지 형태의 검색 결과
     */
    @Override
    public Page<SearchResponse> searchByKeyword(String keyword, Pageable pageable) {
        Page<Search> results = searchRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        return SearchResponse.pageOf(results);
    }

    /**
     * 카테고리 + 태그 필터 검색
     *
     * @param category 카테고리 enum
     * @param tag      태그 enum
     * @param pageable 페이징 정보
     * @return 페이지 형태의 필터 결과
     */
    @Override
    public Page<SearchResponse> searchByFilter(PostCategory category, PostTag tag, Pageable pageable) {
        Page<Search> results = searchRepository.findByCategoryAndTag(category.name(), tag.name(), pageable);
        return SearchResponse.pageOf(results);
    }
}
