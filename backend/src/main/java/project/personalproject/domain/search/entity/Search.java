package project.personalproject.domain.search.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import project.personalproject.domain.post.post.entity.Post;

import java.time.LocalDateTime;

@Document(indexName = "posts") // Elasticsearch 인덱스명
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    private String id; // postId를 문자열로 저장 (Elastic 문서 ID)

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title; // 게시글 제목 (한글 형태소 분석기 사용)

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content; // 게시글 본문 (형태소 분석)

    @Field(type = FieldType.Keyword)
    private String tag; // 태그 (enum name 저장)

    @Field(type = FieldType.Keyword)
    private String category; // 카테고리 (enum name 저장)

    private LocalDateTime createdAt; // 작성일 (ISO 문자열)

    /**
     * Post 엔티티로부터 검색용 문서 객체 생성
     *
     * @param post 게시글 엔티티
     * @return Search 인덱스 객체
     */
    public static Search from(Post post) {
        return Search.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag().name()) // enum → 문자열
                .category(post.getCategory().name()) // enum → 문자열
                .createdAt(post.getCreateDate())
                .build();
    }
}
