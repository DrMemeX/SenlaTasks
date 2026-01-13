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

public class UpdateBookAction implements IAction {

    private final BookService service;

    public UpdateBookAction(BookService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Обновить книгу";
    }

    @Override
    public void execute() {

        long id;
        try {
            id = Long.parseLong(In.get().line("ID книги: "));
        } catch (NumberFormatException e) {
            throw new UserInputException("ID должен быть целым числом.");
        }

        Book existing = service.getBookById(id);

        String newTitle = In.get().line("Новое название: ");
        String newAuthor = In.get().line("Новый автор: ");

        double newPrice;
        try {
            newPrice = Double.parseDouble(In.get().line("Новая цена: "));
        } catch (NumberFormatException e) {
            throw new UserInputException("Цена должна быть числом.");
        }

        BookStatus newStatus;
        try {
            String statusInput = In.get().line("Статус (AVAILABLE/MISSING): ");
            newStatus = BookStatus.valueOf(statusInput.trim());
        } catch (IllegalArgumentException e) {
            throw new UserInputException("Некорректный статус книги. Допустимые значения: AVAILABLE, MISSING.");
        }

        LocalDate releaseDate;
        LocalDate addedDate;
        try {
            releaseDate = LocalDate.parse(In.get().line("Дата выпуска (ГГГГ-ММ-ДД): "));
            addedDate = LocalDate.parse(In.get().line("Дата добавления (ГГГГ-ММ-ДД): "));
        } catch (DateTimeParseException e) {
            throw new UserInputException("Некорректный формат даты. Используйте формат ГГГГ-ММ-ДД.");
        }

        String newDescription = In.get().line("Описание: ");

        existing.setTitle(newTitle);
        existing.setAuthor(newAuthor);
        existing.setPrice(newPrice);
        existing.setStatus(newStatus);
        existing.setReleaseDate(releaseDate);
        existing.setAddedDate(addedDate);
        existing.setDescription(newDescription);

        service.updateBook(existing);
        ConsoleView.ok("Книга успешно обновлена.");
    }
}
