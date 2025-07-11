package project.personalproject.domain.search.entity;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;

@Document(indexName = "posts")
public class Search {

    @Id
    private String id;

    private String title;

    private String content;

    private PostTag tags;

    private PostCategory category;


}
