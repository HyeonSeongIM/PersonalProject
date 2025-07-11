package project.personalproject.domain.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.entity.Search;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<Search, Long> {

    List<Search> findByTitleContainingOrContentContaining(String title, String content);

    List<Search> findByCategoryAndTags(PostCategory category, PostTag tag);
}
