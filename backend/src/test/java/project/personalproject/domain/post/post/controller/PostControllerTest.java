package project.personalproject.domain.post.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.personalproject.domain.post.comment.service.PostCommentService;
import project.personalproject.domain.post.post.dto.request.CreatePostCommand;
import project.personalproject.domain.post.post.service.PostService;
import project.personalproject.global.security.jwt.JwtService;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PostCommentService postCommentService;

    @Test
    @DisplayName("제목이 30자 이상시 에러 핸들링")
    void title_30words_over() throws Exception {
        // given: 31자 이상 제목
        String longTitle = "12345678910_12345678910_12345678910";
        var postJson = new MockMultipartFile(
                "postRequest", "", "application/json",
                objectMapper.writeValueAsBytes(new CreatePostCommand(longTitle, "내용"))
        );

        // when & then
        mockMvc.perform(multipart("/api/v1/post")
                        .file(postJson)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("title"));

        verifyNoInteractions(postService);
    }

    @Test
    @DisplayName("글 내용이 존재하지 않으면 에러 핸들링")
    void content_not_found() throws Exception {
        // given : 내용 X
        var postJson = new MockMultipartFile(
                "postRequest", "", "application/json",
                objectMapper.writeValueAsBytes(new CreatePostCommand("테스트", ""))
        );

        // when & then
        mockMvc.perform(multipart("/api/v1/post")
                        .file(postJson)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("content"));

        verifyNoInteractions(postService);
    }
}
