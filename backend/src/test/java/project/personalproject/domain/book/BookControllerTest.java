package project.personalproject.domain.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;


@GraphQlTest(
        controllers = BookController.class
)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void 책_정보_가져오기() {
        graphQlTester
                .documentName("books")
                .execute()
                .path("books")
                .entityList(Book.class)
                .hasSize(4);
    }

}