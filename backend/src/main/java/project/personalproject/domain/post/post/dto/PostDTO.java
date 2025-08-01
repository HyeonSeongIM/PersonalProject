package project.personalproject.domain.post.post.dto;

import org.springframework.data.domain.Page;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostDTO(
        Long id,
        String title,
        String content,
        Member member,
        List<PostImage> images,
        List<PostComment> comments,
        PostCategory category,
        PostTag postTag,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * 단일 Post 엔티티를 PostResponse DTO로 변환
     *
     * @param post 게시글 엔티티
     * @return 변환된 PostResponse
     */
    public static PostDTO of(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember(),
                post.getImages(),
                post.getComments(),
                post.getCategory(),
                post.getTag(),
                post.getCreateDate(),
                post.getModifyDate()
        );
    }

    /**
     * Post 리스트를 PostResponse 리스트로 변환
     *
     * @param posts 게시글 리스트
     * @return 변환된 PostResponse 리스트
     */
    public static List<PostDTO> listOf(List<Post> posts) {
        return posts.stream()
                .map(PostDTO::of)
                .collect(Collectors.toList());
    }

    /**
     * 페이징된 Post(Page)를 PostResponse Page로 변환
     *
     * @param posts 페이징된 게시글 엔티티
     * @return 변환된 페이징된 PostResponse
     */
    public static Page<PostDTO> pageOf(Page<Post> posts) {
        return posts.map(PostDTO::of);
    }
}
