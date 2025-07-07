package project.personalproject.domain.post.post.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
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

    /**
     * 게시글 빌더 로직입니다.
     * - 각 서비스마다 코드를 넣지 않고 엔티티에 코드를 넣어 유지보수 용이성을 높입니다.
     * @param postRequest
     * @return Post
     */
    public static Post from(CreatePostCommand postRequest, Member member) {
        return Post.builder()
                .title(postRequest.title())
                .content(postRequest.content())
                .member(member)
                .build();
    }

    public static Post updateFrom(Long postId, CreatePostCommand postRequest) {

    }

}
