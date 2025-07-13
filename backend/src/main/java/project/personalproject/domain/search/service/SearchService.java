package project.personalproject.domain.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.dto.response.SearchResponse;

public interface SearchService {
    Page<SearchResponse> searchByKeyword(String keyword, Pageable pageable);

    Page<SearchResponse> searchByFilter(PostCategory category, PostTag tag, Pageable pageable);
}
