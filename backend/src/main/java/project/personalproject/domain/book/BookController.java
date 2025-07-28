package project.personalproject.domain.book;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    @QueryMapping
    public static List<Book> books() {
        return Book.books;
    }

    @QueryMapping
    public static Optional<Book> bookById(@Argument Integer id) {
        return Book.getBookById(id);
    }
}
