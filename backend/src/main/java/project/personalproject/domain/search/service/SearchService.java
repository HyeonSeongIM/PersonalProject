package project.personalproject.domain.search.service;

import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.entity.Search;

import java.util.List;

public interface SearchService {
    List<Search> searchByKeyword(String keyword);

    List<Search> searchByFilter(PostCategory category, PostTag tag, int page, int size);
}
