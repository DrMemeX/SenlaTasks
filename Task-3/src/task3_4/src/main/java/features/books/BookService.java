package features.books;

import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import cvs.applier.BookCsvDtoApplier;
import cvs.exporter.BookCsvExporter;
import cvs.importer.BookCsvImporter;
import exceptions.domain.BookNotFoundException;
import exceptions.domain.DomainException;
import features.requests.RequestService;

import java.util.List;

@Component
@Singleton
public class BookService {

    @Inject
    private BookRepository repo;

    @Inject
    private RequestService requestService;

    public BookService() {
    }

    public BookRepository getBookRepository() {
        return repo;
    }

    public List<Book> getAllBooks() {
        return repo.findAllBooks();
    }

    public Book getBookById(long id) {
        Book book = repo.findBookById(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public Book getBookByTitle(String title) {
        Book book = repo.findBookByTitle(title);
        if (book == null) {
            throw new DomainException("Книга с названием \"" + title + "\" не найдена.");
        }
        return book;
    }

    public Book getBookByTitleAndAuthor(String title, String author) {
        Book book = repo.findBookByTitleAndAuthor(title, author);
        if (book == null) {
            throw new DomainException(
                    "Книга \"" + title + "\" автора \"" + author + "\" не найдена."
            );
        }
        return book;
    }

    public void addBook(Book book) {
        repo.addBook(book);
        try {
            requestService.fulfillRequest(book);
        } catch (Exception ignored) {
        }
    }

    public void deleteBook(long id) {
        boolean removed = repo.deleteBookById(id);
        if (!removed) {
            throw new BookNotFoundException(id);
        }
    }

    public void updateBook(Book incoming) {
        Book existing = repo.findBookById(incoming.getId());
        if (existing == null) {
            throw new BookNotFoundException(incoming.getId());
        }

        existing.setTitle(incoming.getTitle());
        existing.setAuthor(incoming.getAuthor());
        existing.setPrice(incoming.getPrice());
        existing.setStatus(incoming.getStatus());
        existing.setReleaseDate(incoming.getReleaseDate());
        existing.setAddedDate(incoming.getAddedDate());
        existing.setDescription(incoming.getDescription());

        repo.updateBook(existing);
    }

    public void importBooksFromCsv(String filePath,
                                   BookCsvImporter importer,
                                   BookCsvDtoApplier applier) {
        List<BookCsvDto> dtos = importer.importFrom(filePath);
        for (BookCsvDto dto : dtos) {
            applier.apply(dto);
        }
    }

    public void exportBooksToCsv(String filePath,
                                 BookCsvExporter exporter) {
        exporter.exportTo(filePath, repo.findAllBooks());
    }
}
