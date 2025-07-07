package project.personalproject.domain.post.post.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.global.util.BaseTimeEntity;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
