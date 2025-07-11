package project.personalproject.domain.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.personalproject.domain.search.dto.request.SearchFilter;
import project.personalproject.domain.search.dto.request.SearchRequest;
import project.personalproject.domain.search.dto.response.SearchResponse;
import project.personalproject.domain.search.service.SearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    // TODO : 키워드 기반 검색 API 구성
    @GetMapping
    public ResponseEntity<List<SearchResponse>> search(SearchRequest searchRequest) {
        throw new UnsupportedOperationException("구성중");
    }

    // TODO : 카테고리 및 태그 기반 검색 API 구성
    @GetMapping("/filter")
    public ResponseEntity<List<SearchFilter>> filter(SearchFilter searchFilter, Pageable pageable) {
        throw new UnsupportedOperationException("구성중");
    }
}
