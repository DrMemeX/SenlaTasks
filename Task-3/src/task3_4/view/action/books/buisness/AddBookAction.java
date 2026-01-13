package task3_4.view.action.books.buisness;

import task3_4.common.status.BookStatus;
import task3_4.exceptions.ui.UserInputException;
import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddBookAction implements IAction {

    private final BookService service;

    public AddBookAction(BookService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Добавить книгу";
    }

    @Override
    public void execute() {
        String title = In.get().line("Название: ");
        String author = In.get().line("Автор: ");

        double price;
        String priceInput = In.get().line("Цена: ");
        try {
            price = Double.parseDouble(priceInput);
        } catch (NumberFormatException e) {
            throw new UserInputException("Цена должна быть числом.");
        }
        if (price <= 0) {
            throw new UserInputException("Цена должна быть больше нуля.");
        }

        LocalDate release;
        String dateInput = In.get().line("Дата выпуска (ГГГГ-ММ-ДД): ");
        try {
            release = LocalDate.parse(dateInput);
        } catch (DateTimeParseException e) {
            throw new UserInputException("Некорректная дата. Используйте формат ГГГГ-ММ-ДД.");
        }
        if (release.isAfter(LocalDate.now())) {
            throw new UserInputException("Дата выпуска не может быть в будущем.");
        }

        String descr = In.get().line("Описание: ");

        Book b = new Book(
                title, author, price, BookStatus.AVAILABLE,
                release, LocalDate.now(), descr
        );

        service.addBook(b);
        ConsoleView.ok("Книга добавлена.");
    }
}
