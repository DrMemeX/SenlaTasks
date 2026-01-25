package view.action.requests.buisness;

import cvs.applier.RequestCsvDtoApplier;
import cvs.importer.RequestCsvImporter;
import exceptions.csv.CsvException;
import features.requests.RequestService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class ImportRequestsAction implements IAction {

    private final RequestService service;

    public ImportRequestsAction(RequestService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Импорт запросов из CSV";
    }

    @Override
    public void execute() {
        try {
            String path = In.get().line("Введите путь к CSV: ");

            service.importRequestsFromCsv(
                    path,
                    new RequestCsvImporter(),
                    new RequestCsvDtoApplier(
                            service.getRequestRepository(),
                            service.getBookRepository(),
                            service.getOrderRepository()
                    )
            );

            ConsoleView.ok("Импорт запросов завершён.");
        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        }
    }
}
