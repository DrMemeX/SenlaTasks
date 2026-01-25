package view.action.books.buisness;

import exceptions.ui.UserInputException;
import features.books.BookService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
