package view.action.books.buisness;

import cvs.exporter.BookCsvExporter;
import exceptions.csv.CsvException;
import features.books.BookService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
        }
    }
}
