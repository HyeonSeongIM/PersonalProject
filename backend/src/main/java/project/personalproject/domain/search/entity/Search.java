package project.personalproject.domain.search.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import project.personalproject.domain.post.post.entity.Post;

import java.time.LocalDateTime;

@Document(indexName = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String content;

    @Field(type = FieldType.Keyword)
    private String tag; // PostTag -> name() 또는 value로 매핑

    @Field(type = FieldType.Keyword)
    private String category; // PostCategory -> name() 또는 value로 매핑

    private String createdAt;

    public static Search from(Post post) {
        return Search.builder()
                .id(post.getId().toString())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag().name()) // Tag 처리 필요 시 여기에서 처리
                .category(post.getCategory().name()) // Category 처리 필요 시 여기에서 처리
                .createdAt(post.getCreateDate().toString())
                .build();
    }
}
