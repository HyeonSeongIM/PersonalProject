package project.personalproject.domain.book;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Book(
        Integer id,
        String name,
        Integer pageCount,
        Integer authorId
) {
    public static List<Book> books = Arrays.asList(
            new Book(1, "A", 600, 10),
            new Book(2, "B", 601, 12),
            new Book(3, "C", 602, 11),
            new Book(4, "D", 603, 13)
    );

    public static Optional<Book> getBookById(Integer id) {
        return books.stream()
                .filter(b -> b.id.equals(id))
                .findFirst();
    }
}
