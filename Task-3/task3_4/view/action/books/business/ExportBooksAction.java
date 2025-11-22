package task3_4.view.action.books.business;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.features.books.BookCsvExporter;
import task3_4.features.books.BookService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class ExportBooksAction implements IAction {

    private final BookService service;

    public ExportBooksAction(BookService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Экспорт книг (CSV)";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь для сохранения CSV: ");

            service.exportBooksToCsv(
                    path,
                    new BookCsvExporter()
            );

            ConsoleView.ok("Экспорт книг завершён.");

        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Не удалось сохранить файл: " + e.getMessage());
        }
    }
}
