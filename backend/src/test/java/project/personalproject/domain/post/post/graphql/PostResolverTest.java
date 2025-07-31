package project.personalproject.domain.post.post.graphql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.image.service.PostImageService;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.post.post.graphql.dto.PostDTO;
import project.personalproject.domain.post.post.graphql.dto.PostListDTO;
import project.personalproject.domain.post.post.service.PostService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@GraphQlTest(controllers = PostResolver.class)
class PostResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private PostImageService postImageService;    // ← 추가

    @MockitoBean
    private PostCommentService postCommentService; // ← 추가

    @Test
    @DisplayName("단건 게시글 조회 정상 확인")
    void getPostById() {
        // given
        Long postId = 1L;

        Member member = Member.builder().id(1L).build();

        PostDTO postDTO = new PostDTO(
                postId,
                "테스트 1",
                "테스트 진행",
                member,
                null,
                null,
                PostCategory.Backend,
                PostTag.Optimization,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postService.getPost(postId)).thenReturn(postDTO);

        // when & given
        graphQlTester.documentName("Post")
                .operationName("getPost")
                .variable("id", postId)
                .execute()
                .path("getPost")
                .entity(PostDTO.class)
                .satisfies(post -> {
                    assertEquals(postId, post.id());
                    assertEquals("테스트 1", post.title());
                    assertEquals("테스트 진행", post.content());
                    assertEquals(member.getId(), post.member().getId());
                    assertEquals(null, post.category());
                    assertEquals(null, post.postTag());
                });
    }

    @Test
    @DisplayName("게시글 리스트 반환")
    void getPostList() {
        // given
        Long postId = 1L;

        Member member = Member.builder().id(1L).build();

        PostDTO postDTO1 = new PostDTO(
                postId,
                "테스트 1",
                "테스트 진행",
                member,
                null,
                null,
                PostCategory.Backend,
                PostTag.Optimization,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        PostDTO postDTO2 = new PostDTO(
                postId,
                "테스트 2",
                "테스트 진행",
                member,
                null,
                null,
                PostCategory.Backend,
                PostTag.Optimization,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        List<PostDTO> postDTOList = List.of(
                postDTO1, postDTO2
        );

        PostListDTO postListDTO = new PostListDTO(
                postDTOList, 5, 2, 0, 1
        );

        when(postService.getPostList(PageRequest.of(0,1))).thenReturn(postListDTO);

        // when
        graphQlTester.documentName("Post")
                .operationName("getPostList")
                .execute()
                .path("getPostList")
                .entity(PostListDTO.class)
                .satisfies(postList -> {
                    assertEquals(postListDTO.posts(), postDTOList);
                    assertEquals(postListDTO.size(), postList.size());
                });

    }

}