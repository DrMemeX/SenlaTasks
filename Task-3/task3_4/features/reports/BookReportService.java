package task3_4.features.reports;

import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.common.status.BookStatus;
import task3_4.view.util.ConsoleView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookReportService {

    private final BookService bookService;

    public BookReportService(BookService bookService) {
        this.bookService = bookService;
    }

    public void showBooksSortedByTitle(boolean ascending) {
        List<Book> books = bookService.getAllBooks();

        Comparator<Book> cmp = Comparator.comparing(Book::getTitle);
        if (!ascending) cmp = cmp.reversed();

        books = books.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Книги по алфавиту (А → Я)"
                : "Книги по алфавиту (Я → А)");

        for (Book b : books) {
            ConsoleView.info(b.toString());
        }
    }

    public void showBooksSortedByReleaseDate(boolean ascending) {
        List<Book> books = bookService.getAllBooks();

        Comparator<Book> cmp = Comparator.comparing(Book::getReleaseDate);
        if (!ascending) cmp = cmp.reversed();

        books = books.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Книги по дате выпуска (старые → новые)"
                : "Книги по дате выпуска (новые → старые)");

        for (Book b : books) {
            ConsoleView.info(b.toString());
        }
    }

    public void showBooksSortedByPrice(boolean ascending) {
        List<Book> books = bookService.getAllBooks();

        Comparator<Book> cmp = Comparator.comparing(Book::getPrice);
        if (!ascending) cmp = cmp.reversed();

        books = books.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Книги по цене (дешёвые → дорогие)"
                : "Книги по цене (дорогие → дешёвые)");

        for (Book b : books) {
            ConsoleView.info(b.toString());
        }
    }

    public void showBooksSortedByAvailability(boolean ascending) {
        List<Book> books = bookService.getAllBooks();

        Comparator<Book> cmp = Comparator.comparing(Book::getStatus);
        if (!ascending) cmp = cmp.reversed();

        books = books.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Книги по наличию (в наличии → отсутствуют)"
                : "Книги по наличию (отсутствуют → в наличии)");

        for (Book b : books) {
            ConsoleView.info(b.toString());
        }
    }

    public void showOldBooks(boolean sortByDate, boolean sortByPrice, boolean ascending) {
        List<Book> books = bookService.getAllBooks();
        LocalDate now = LocalDate.now();

        List<Book> oldBooks = books.stream()
                .filter(b -> b.getAddedDate() != null)
                .filter(b -> ChronoUnit.MONTHS.between(b.getAddedDate(), now) > 6)
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .collect(Collectors.toList());

        if (oldBooks.isEmpty()) {
            ConsoleView.title("Залежавшиеся книги");
            ConsoleView.warn("Нет книг, залежавшихся более 6 месяцев.");
            return;
        }

        Comparator<Book> cmp;

        if (sortByDate) {
            ConsoleView.info("Сортировка по дате поступления:");
            cmp = Comparator.comparing(Book::getAddedDate);
        } else if (sortByPrice) {
            ConsoleView.info("Сортировка по цене:");
            cmp = Comparator.comparing(Book::getPrice);
        } else {
            ConsoleView.info("Не указано поле сортировки, используется дата поступления:");
            cmp = Comparator.comparing(Book::getAddedDate);
        }

        if (!ascending) cmp = cmp.reversed();

        oldBooks = oldBooks.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Залежавшиеся книги (по возрастанию)"
                : "Залежавшиеся книги (по убыванию)");

        for (Book b : oldBooks) {
            ConsoleView.info(
                    "Название: " + b.getTitle() +
                            " | Дата поступления: " + b.getAddedDate() +
                            " | Цена: " + b.getPrice() + " руб."
            );
        }
    }

    public void showBookDescription(Book book) {
        if (book == null) {
            ConsoleView.warn("Книга не найдена.");
            return;
        }

        String d = (book.getDescription() == null || book.getDescription().isBlank())
                ? "— описание не указано —"
                : book.getDescription().trim();

        ConsoleView.title("Описание книги");
        ConsoleView.info("Название: " + book.getTitle());
        ConsoleView.hr();
        ConsoleView.info(d);
    }

    public void showBookDescription(String title) {
        Book b = bookService.getBookByTitle(title);
        showBookDescription(b);
    }

    public void showBookDescription(String title, String author) {
        Book b = bookService.getBookByTitleAndAuthor(title, author);
        showBookDescription(b);
    }
}
