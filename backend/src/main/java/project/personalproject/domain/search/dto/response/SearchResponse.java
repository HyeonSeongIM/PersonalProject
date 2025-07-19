package project.personalproject.domain.search.dto.response;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.search.entity.Search;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SearchResponse(
        String postId,      // 게시글 ID
        String title,           // 제목
        String content,  // 내용 일부 (예: 100자 요약)
        PostCategory category,        // 카테고리명
        PostTag tag,      // 태그들 (문자열로)
        LocalDateTime createdAt       // 작성 시간 (선택)
) {

    public static SearchResponse of(Search search) {
        return new SearchResponse(
                search.getId(),
                search.getTitle(),
                search.getContent(),
                PostCategory.valueOf(search.getCategory()),
                PostTag.valueOf(search.getTag()),
                search.getCreatedAt()
        );
    }

    public static List<SearchResponse> listOf (List<Search> searches) {
        return searches.stream()
                .map(SearchResponse::of)
                .collect(Collectors.toList());
    }

    public static Page<SearchResponse> pageOf (Page<Search> searches) {
        return searches.map(SearchResponse::of);
    }
}
