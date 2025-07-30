package project.personalproject.domain.post.post.graphql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import project.personalproject.domain.post.post.entity.Post;

@GraphQlTest(controllers = PostResolver.class)
class PostResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    @DisplayName("단건 게시글 조회 정상 확인")
    void getPostById() {
        graphQlTester.documentName("getPostById.graphql")
                .execute()
                .path("getPostById")
                .entityList(Post.class)
                .hasSize(4);
    }

}