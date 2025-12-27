package task3_4.view.action.books.business;

import task3_4.exceptions.domain.DomainException;
import task3_4.features.books.BookService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class DeleteBookAction implements IAction {

    private final BookService service;

    public DeleteBookAction(BookService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Удалить книгу";
    }

    @Override
    public void execute() {

        try {
            long id = Long.parseLong(In.get().line("Введите ID книги: "));

            service.deleteBook(id);

            ConsoleView.ok("Книга успешно удалена.");

        } catch (NumberFormatException e) {
            ConsoleView.warn("Некорректный формат числа.");
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        }
    }
}
