package task3_4.features.books;

import task3_4.cvs.applier.BookCsvDtoApplier;
import task3_4.cvs.exporter.BookCsvExporter;
import task3_4.cvs.importer.BookCsvImporter;
import task3_4.exceptions.domain.BookNotFoundException;
import task3_4.exceptions.domain.DomainException;
import task3_4.features.requests.RequestService;

import java.util.List;

public class BookService {

    private final BookRepository repo;
    private final RequestService requestService;

    public BookService(BookRepository repo, RequestService requestService) {
        this.repo = repo;
        this.requestService = requestService;
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
