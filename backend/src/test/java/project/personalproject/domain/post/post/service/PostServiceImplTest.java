package project.personalproject.domain.post.post.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.comment.entity.PostComment;
import project.personalproject.domain.post.image.entity.PostImage;
import project.personalproject.domain.post.post.entity.Post;
import project.personalproject.domain.post.post.entity.PostCategory;
import project.personalproject.domain.post.post.entity.PostTag;
import project.personalproject.domain.post.post.dto.PostDTO;
import project.personalproject.domain.post.post.repository.PostRepository;
import project.personalproject.domain.post.post.service.impl.PostServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// DTO ↔ Entity 직접 비교 지양

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("### Post Service Test Start ###");
    }

    @AfterEach
    public void setDown() {
        System.out.println("### Post Service Test Finish ###");
    }

    @Test
    @DisplayName("생성된 게시글을 정확히 받아와야 한다.")
    void getPost_Title_Content() {
        // given
        Long postId = 1L;

        Post post = Post.builder()
                .id(postId)
                .title("안녕하세요.")
                .content("테스트입니다.")
                .category(PostCategory.Backend)
                .tag(PostTag.TechnologyAnalysis)
                .build();

        when(postRepository.findByIdOrThrow(postId)).thenReturn(post);

        // when
        PostDTO postDTO = postService.getPost(postId);

        // then
        assertEquals(postId,          postDTO.id());
        assertEquals(post.getTitle(),   postDTO.title());
        assertEquals(post.getContent(), postDTO.content());
        assertEquals(post.getCategory(), postDTO.category());
        assertEquals(post.getTag(),      postDTO.postTag());

        verify(postRepository).findByIdOrThrow(postId);
    }

    @Test
    @DisplayName("이미지가 있는 게시글은 이미지 리스트까지 매핑된다")
    void getPost_With_Image() {
        // given
        Long postId = 1L;

        Post post = Post.builder()
                .id(postId)
                .title("테스트 2입니다.")
                .content("이미지도 있습니다.")
                .build();

        List<PostImage> postImages = Arrays.asList(
                new PostImage(1L, "이미지1", post),
                new PostImage(2L, "이미지2", post)
        );

        post.setImages(postImages);

        // when
        when(postRepository.findByIdOrThrow(postId)).thenReturn(post);

        PostDTO postDTO = postService.getPost(postId);

        // then
        assertEquals(postId,           postDTO.id());
        assertEquals(post.getTitle(),    postDTO.title());
        assertEquals(post.getContent(),  postDTO.content());
        assertEquals(post.getImages(),   postDTO.images());

        verify(postRepository).findByIdOrThrow(postId);
    }

    @Test
    @DisplayName("댓글이 있는 게시글은 댓글 리스트까지 매핑된다")
    void getPost_With_Comment() {
        // given
        Long postId = 1L;

        Member member = Member.builder()
                .id(12L)
                .build();

        Post post = Post.builder()
                .id(postId)
                .title("테스트 3입니다.")
                .content("댓글들 한번에 가져오기")
                .build();

        List<PostComment> postComments = Arrays.asList(
                new PostComment(123L, "하하", post, member),
                new PostComment(124L, "하하", post, member),
                new PostComment(125L, "하하", post, member)
        );

        post.setComments(postComments);

        // when
        when(postRepository.findByIdOrThrow(postId)).thenReturn(post);

        PostDTO postDTO = postService.getPost(postId);

        // then
        assertEquals(postId,            postDTO.id());
        assertEquals(post.getTitle(),     postDTO.title());
        assertEquals(post.getContent(),   postDTO.content());
        assertEquals(post.getComments(),  postDTO.comments());

        verify(postRepository).findByIdOrThrow(postId);
    }
}