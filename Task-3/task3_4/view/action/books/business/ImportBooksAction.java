package task3_4.view.action.books.business;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.features.books.BookCsvDtoApplier;
import task3_4.features.books.BookCsvImporter;
import task3_4.features.books.BookService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class ImportBooksAction implements IAction {

    private final BookService service;

    public ImportBooksAction(BookService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Импорт книг (CSV)";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь к CSV-файлу: ");

            service.importBooksFromCsv(
                    path,
                    new BookCsvImporter(),
                    new BookCsvDtoApplier(service.getBookRepository())
            );

            ConsoleView.ok("Импорт книг завершён.");

        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Не удалось выполнить импорт: " + e.getMessage());
        }
    }
}
