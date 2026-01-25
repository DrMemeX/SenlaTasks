package features.books;

import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import dao.jdbc.BookJdbcDao;

import java.sql.Connection;
import java.util.List;

@Component
@Singleton
public class BookRepository {

    @Inject
    private BookJdbcDao dao;

    public List<Book> findAllBooks() {
        return dao.findAll();
    }

    public Book findBookById(long id) {
        return dao.findById(id).orElse(null);
    }

    public Book findBookByTitle(String title) {
        return dao.findByTitle(title).orElse(null);
    }

    public Book findBookByTitleAndAuthor(String title, String author) {
        return dao.findByTitleAndAuthor(title, author).orElse(null);
    }

    public void addBook(Book book) {
        dao.create(book);
    }

    public boolean deleteBookById(long id) {
        return dao.deleteById(id);
    }

    public void updateBook(Book book) {
        dao.update(book);
    }
    public void updateBook(Connection c, Book book) {
        dao.update(c, book);
    }

    public void restoreState(List<Book> bookState) {
    }
}
