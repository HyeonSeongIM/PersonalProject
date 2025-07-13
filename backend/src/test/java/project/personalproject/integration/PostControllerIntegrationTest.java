package project.personalproject.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.personalproject.domain.post.post.repository.PostRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
public class PostControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Test
    void 통합_테스트는_엔티티주고_실행만_하면_된다() throws Exception {
        // given

        // when & then
        
    }
}
