package project.personalproject.post.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import project.personalproject.post.dto.request.PostRequest;
import project.personalproject.post.entity.Post;
import project.personalproject.post.repository.PostRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Test
    void 게시글_생성_LONG_타입_반환_여부_확인() {

        // given
        PostRequest postRequest = new PostRequest("title", "content");

        Post post = new Post();

        ReflectionTestUtils.setField(post, "id", 1L);

        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());

        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        Long resultId = postService.create(postRequest);

        // then
        assertTrue(resultId instanceof Long);
    }

    @Test
    void 게시글_생성_엔티티_생성_여부_확인() {

        //given
        PostRequest postRequest = new PostRequest("title", "content");

        Post post = new Post();

        ReflectionTestUtils.setField(post, "id", 1L);

        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());

        given(postRepository.save(any(Post.class))).willReturn(post);

        //when
        postService.create(postRequest);

        //then
        verify(postRepository).save(any(Post.class));

    }

    @Test
    void 게시글_생성_DTO_빈값_예외처리_여부(){

        // given
        PostRequest postRequest = new PostRequest("", "content");

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> postService.create(postRequest));


    }

}