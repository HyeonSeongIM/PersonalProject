package project.personalproject.domain.post.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostService_CreateTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("제목이 30자 이상시 에러 핸들링")
    void title_30words_over() {
        //.given
        CreatePostCommand postDTO = new CreatePostCommand("가나다라마바사아자차카타하가나다라마바사아자차카타하가나다라마바사아자차카타하", "테스트 입니다.");


        // when


        // then


    }
}
