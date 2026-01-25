package view.action.books.buisness;

import cvs.applier.BookCsvDtoApplier;
import cvs.importer.BookCsvImporter;
import exceptions.csv.CsvException;
import features.books.BookService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
        }
    }
}
