package task3_4.view.action.books.buisness;

import task3_4.exceptions.ui.UserInputException;
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
        long id;
        try {
            id = Long.parseLong(In.get().line("Введите ID книги: "));
        } catch (NumberFormatException e) {
            throw new UserInputException("Некорректный формат числа.");
        }

        service.deleteBook(id);
        ConsoleView.ok("Книга успешно удалена.");
    }
}
