package project.personalproject.domain.post.post.dto.response;

import org.springframework.data.domain.Page;
import project.personalproject.domain.post.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 응답 DTO
 * - Entity(Post)로부터 데이터를 받아 외부에 전달할 정보를 담는다.
 * - 불변성과 명확한 역할 분리를 위해 record 사용
 */
public record PostResponse(
        String title,
        String content,
        String memberName,
        LocalDateTime createDate
) {
    /**
     * 단일 Post 엔티티를 PostResponse DTO로 변환
     *
     * @param post 게시글 엔티티
     * @return 변환된 PostResponse
     */
    public static PostResponse of(Post post) {
        return new PostResponse(
                post.getTitle(),
                post.getContent(),
                post.getMember().getUsername(),
                post.getCreateDate()
        );
    }

    /**
     * Post 리스트를 PostResponse 리스트로 변환
     *
     * @param posts 게시글 리스트
     * @return 변환된 PostResponse 리스트
     */
    public static List<PostResponse> listOf(List<Post> posts) {
        return posts.stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 페이징된 Post(Page)를 PostResponse Page로 변환
     *
     * @param posts 페이징된 게시글 엔티티
     * @return 변환된 페이징된 PostResponse
     */
    public static Page<PostResponse> pageOf(Page<Post> posts) {
        return posts.map(PostResponse::of);
    }
}
