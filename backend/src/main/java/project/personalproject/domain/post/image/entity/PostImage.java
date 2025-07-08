package project.personalproject.domain.post.image.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.post.post.entity.Post;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageName;

    /**
     * 게시글과의 연관관계
     * - 단방향이 아닌 양방향으로 연결
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
