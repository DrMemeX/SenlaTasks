package task3_4.features.books;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;

import java.util.ArrayList;
import java.util.List;

@Component
@Singleton
public class BookRepository {

    private final List<Book> bookList = new ArrayList<>();

    public List<Book> findAllBooks() {
        return bookList;
    }

    public Book findBookById(long id) {
        return bookList.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Book findBookByTitle(String title) {
        return bookList.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public Book findBookByTitleAndAuthor(String title, String author) {
        return bookList.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(title)
                        && b.getAuthor().equalsIgnoreCase(author))
                .findFirst()
                .orElse(null);
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public boolean deleteBookById(long id) {
        return bookList.removeIf(b -> b.getId() == id);
    }

    public void restoreState(List<Book> bookState) {
        if (bookState != null) {
            bookList.clear();
            bookList.addAll(bookState);
        }
    }
}