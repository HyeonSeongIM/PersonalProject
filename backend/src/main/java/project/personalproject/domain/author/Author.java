package project.personalproject.domain.author;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Author(
        Integer id,
        String name
) {
    public static List<Author> authors = Arrays.asList(
            new Author(1, "J"),
            new Author(2, "K"),
            new Author(3, "L"),
            new Author(13, "M"),
            new Author(14, "O")

    );

    public static Optional<Author> getAuthorById(Integer id) {
        return authors.stream()
                .filter(b -> b.id.equals(id))
                .findFirst();
    }
}
