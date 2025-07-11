package project.personalproject.domain.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import project.personalproject.domain.search.entity.Search;

public interface SearchRepository extends ElasticsearchRepository<Search, Long> {

    Page<Search> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Search> findByCategoryAndTag(String category, String tag, Pageable pageable);
}
