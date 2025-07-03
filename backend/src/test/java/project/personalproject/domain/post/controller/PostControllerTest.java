package project.personalproject.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import project.personalproject.domain.post.dto.request.PostRequest;
import project.personalproject.domain.post.exception.PostException;
import project.personalproject.domain.post.service.PostService;
import project.personalproject.global.exception.ErrorCode;
import project.personalproject.global.security.SecurityConfigTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(SecurityConfigTest.class)
@WebMvcTest(PostService.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private ErrorCode errorCode;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void 게시글_생성_API_200응답_여부_확인() throws Exception {
        // given
        PostRequest postRequest = new PostRequest("title", "content");

        // when & then
        mockMvc.perform(post("/api/v1/post/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void 게시글_생성_시_값을_잘_받아오는지_확인() throws Exception {
        // given
        PostRequest postRequest = new PostRequest("title", "content");

        given(postService.create(any(PostRequest.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/v1/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1)); // JsonBody 에서 ID 필드가 있을 것이고, 거기서 1일거야 를 가르킴

        verify(postService).create(any(PostRequest.class));
    }

    @Test
    void 게시글_생성_시_빈값_에러_처리_여부() throws Exception {
        // given
        PostRequest postRequest = new PostRequest("", "content");

        given(postService.create(any(PostRequest.class)))
                .willThrow(new PostException(ErrorCode.INVALID_POST_REQUEST));

        // when & then
        mockMvc.perform(post("/api/v1/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(postService).create(any(PostRequest.class));
    }

}