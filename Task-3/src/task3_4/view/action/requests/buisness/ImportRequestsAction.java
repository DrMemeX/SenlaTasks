package task3_4.view.action.requests.buisness;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.cvs.applier.RequestCsvDtoApplier;
import task3_4.cvs.importer.RequestCsvImporter;
import task3_4.features.requests.RequestService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

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
