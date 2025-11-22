package task3_4.view.action.books.business;

import task3_4.common.status.BookStatus;
import task3_4.exceptions.domain.DomainException;
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

        try {
            long id = Long.parseLong(In.get().line("ID книги: "));
            Book existing = service.getBookById(id);

            String newTitle = In.get().line("Новое название: ");
            String newAuthor = In.get().line("Новый автор: ");
            double newPrice = Double.parseDouble(In.get().line("Новая цена: "));

            String statusInput = In.get().line("Статус (AVAILABLE/MISSING): ");
            BookStatus newStatus = BookStatus.valueOf(statusInput.trim());

            LocalDate releaseDate = LocalDate.parse(In.get().line("Дата выпуска (YYYY-MM-DD): "));
            LocalDate addedDate = LocalDate.parse(In.get().line("Дата добавления (YYYY-MM-DD): "));

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

        } catch (NumberFormatException e) {
            ConsoleView.warn("Некорректный формат числа.");
        } catch (IllegalArgumentException e) { // BookStatus.valueOf
            ConsoleView.warn("Некорректный статус книги. Допустимые значения: AVAILABLE, MISSING.");
        } catch (DateTimeParseException e) {
            ConsoleView.warn("Некорректный формат даты. Используйте формат YYYY-MM-DD.");
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Ошибка при обновлении книги: " + e.getMessage());
        }
    }
}
