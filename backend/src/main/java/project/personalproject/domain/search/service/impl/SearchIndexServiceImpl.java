package project.personalproject.domain.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.search.entity.Search;
import project.personalproject.domain.search.repository.SearchRepository;
import project.personalproject.domain.search.service.SearchIndexService;

@RequiredArgsConstructor
@Service
public class SearchIndexServiceImpl implements SearchIndexService {

    private final SearchRepository searchRepository;

    /**
     * 게시글을 Elasticsearch 인덱스에 저장 (새 게시글 등록 시)
     *
     * @param post 게시글 엔티티
     */
    @Override
    public void indexPost(Post post) {
        Search index = Search.from(post);
        searchRepository.save(index); // Elastic에 저장
    }

    /**
     * 게시글을 Elasticsearch 인덱스에서 갱신 (수정 시)
     *
     * @param post 게시글 엔티티
     */
    @Override
    public void updatePost(Post post) {
        Search index = Search.from(post);
        searchRepository.save(index); // 동일한 ID로 저장 → 덮어쓰기
    }

    /**
     * 게시글을 Elasticsearch 인덱스에서 삭제 (삭제 시)
     *
     * @param postId 게시글 ID
     */
    @Override
    public void deletePost(Long postId) {
        searchRepository.deleteById(postId.toString()); // Elastic ID는 문자열
    }
}
