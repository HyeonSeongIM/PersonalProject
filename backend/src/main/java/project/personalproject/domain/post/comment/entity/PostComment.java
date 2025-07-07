package project.personalproject.domain.post.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.global.util.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    /**
     * 게시글과의 연관관계
     * - 단방향이 아닌 양방향 관계 유지
     * - mappedBy 사용으로 연관된 Post 객체에서 댓글 리스트 조회 가능
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 댓글 작성자 (삭제/수정 권한 체크 등에 사용됨)
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
