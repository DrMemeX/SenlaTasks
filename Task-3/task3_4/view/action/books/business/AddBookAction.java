package task3_4.view.action.books.business;

import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.common.status.BookStatus;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.time.LocalDate;

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
        double price = Double.parseDouble(In.get().line("Цена: "));
        LocalDate release = LocalDate.parse(In.get().line("Дата выпуска (ГГГГ-ММ-ДД): "));
        String descr = In.get().line("Описание: ");

        Book b = new Book(
                title, author, price, BookStatus.AVAILABLE,
                release, LocalDate.now(), descr
        );

        service.addBook(b);
        ConsoleView.ok("Книга добавлена.");
    }
}
