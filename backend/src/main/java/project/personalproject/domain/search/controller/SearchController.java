package project.personalproject.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.dto.response.SearchResponse;
import project.personalproject.domain.search.service.AutocompleteService;
import project.personalproject.domain.search.service.SearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;
    private final AutocompleteService autocompleteService;

    /**
     * 키워드 기반 게시글 검색
     *
     * @param keyword  검색 키워드 (title 또는 content에 포함)
     * @param pageable 페이징 정보
     * @return 검색 결과
     */
    @GetMapping
    public ResponseEntity<Page<SearchResponse>> searchByKeyword(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(searchService.searchByKeyword(keyword, pageable));
    }

    /**
     * 카테고리 + 태그 기반 필터 검색
     *
     * @param category 카테고리 enum 값
     * @param tag      태그 enum 값
     * @param pageable 페이징 정보
     * @return 필터 검색 결과
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<SearchResponse>> searchByFilter(
            @RequestParam PostCategory category,
            @RequestParam PostTag tag,
            Pageable pageable
    ) {
        return ResponseEntity.ok(searchService.searchByFilter(category, tag, pageable));
    }

    /**
     * 자동완성 추천 API
     *
     * @param prefix 제목 접두어
     * @return 추천 제목 리스트
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(
            @RequestParam String prefix
    ) {
        return ResponseEntity.ok(autocompleteService.suggestTitle(prefix));
    }
}
