package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.service.SearchService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    @Override
    public List<Search> searchByKeyword(String keyword) {
        return List.of();
    }

    @Override
    public List<Search> searchByFilter(PostCategory category, PostTag tag, int page, int size) {
        return List.of();
    }
}
