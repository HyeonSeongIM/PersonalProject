package project.personalproject.domain.post.post.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.dto.request.UpdatePostCommand;
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

    private PostTag tag;

    private PostCategory category;

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
                .tag(PostTag.TechnologyAnalysis)
                .category(PostCategory.Backend)
                .member(member)
                .build();
    }

    /**
     * 게시글 업데이트 빌더 로직입니다.
     * - Post에는 원래 등록되어 있는 Post id 값을 가져오기
     * - UpdatePostCommand 에서 변경 값 가져오기
     * @param post
     * @param postRequest
     * @return
     */
    public static Post updateFrom(Post post, UpdatePostCommand postRequest) {
        return Post.builder()
                .id(post.getId())
                .title(postRequest.title())
                .content(postRequest.content())
                .tag(PostTag.TechnologyAnalysis)
                .category(PostCategory.Backend)
                .build();
    }

}
