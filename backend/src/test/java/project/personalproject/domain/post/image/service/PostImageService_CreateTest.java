package project.personalproject.domain.post.image.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;
import project.personalproject.domain.member.entity.Member;
import project.personalproject.domain.post.image.exception.PostImageException;
import project.personalproject.domain.post.image.repository.PostImageRepository;
import project.personalproject.domain.post.image.service.impl.PostImageServiceImpl;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.entity.Post;

import java.util.List;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.when;

@WithMockUser
@ExtendWith(MockitoExtension.class)
class PostImageService_CreateTest {

    @InjectMocks
    private PostImageServiceImpl postImageService;

    @Mock
    private PostImageRepository postImageRepository;

    @Test
    @DisplayName("이미지 5개가 넘어가면 에러 핸들링")
    void file_count_over_5() throws PostImageException {
        // given : 이미지 6개
        List<MultipartFile> images = List.of(
                new MockMultipartFile("image1", "test1.jpg", "image/jpeg", "test image 1".getBytes()),
                new MockMultipartFile("image2", "test2.jpg", "image/jpeg", "test image 2".getBytes()),
                new MockMultipartFile("image3", "test3.jpg", "image/jpeg", "test image 3".getBytes()),
                new MockMultipartFile("image4", "test4.jpg", "image/jpeg", "test image 4".getBytes()),
                new MockMultipartFile("image5", "test5.jpg", "image/jpeg", "test image 5".getBytes()),
                new MockMultipartFile("image6", "test6.jpg", "image/jpeg", "test image 6".getBytes())
        );

        Post command = mock(Post.class);

        // when & then : createImages
        assertThatThrownBy(() -> postImageService.createImages(command, images))
                .isInstanceOf(PostImageException.class);
    }
}
