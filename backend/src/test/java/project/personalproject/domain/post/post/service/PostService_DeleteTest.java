package project.personalproject.domain.post.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.personalproject.domain.post.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostService_DeleteTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("")
    void createPost() {

    }
}
