package project.personalproject.domain.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import project.personalproject.domain.search.entity.Search;

import java.util.List;

/**
 * Elasticsearch를 통한 검색 저장소 인터페이스.
 * Elastic 문서 ID는 String으로 설정 (postId를 문자열로 저장)
 */
public interface SearchRepository extends ElasticsearchRepository<Search, String> {

    /**
     * 제목 또는 내용에 키워드가 포함된 게시글 검색
     */
    Page<Search> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    /**
     * 카테고리 + 태그가 일치하는 게시글 검색
     */
    Page<Search> findByCategoryAndTag(String category, String tag, Pageable pageable);

    /**
     * 제목이 접두어(prefix)로 시작하는 게시글 10개 검색 (자동완성용)
     */
    List<Search> findTop10ByTitleStartingWith(String prefix);
}
