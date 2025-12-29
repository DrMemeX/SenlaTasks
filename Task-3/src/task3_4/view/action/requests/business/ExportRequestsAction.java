package task3_4.view.action.requests.business;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.cvs.exporter.RequestCsvExporter;
import task3_4.features.requests.RequestService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class ExportRequestsAction implements IAction {

    private final RequestService service;

    public ExportRequestsAction(RequestService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Экспорт запросов в CSV";
    }

    @Override
    public void execute() {
        try {
            String path = In.get().line("Введите путь для сохранения: ");

            service.exportRequestsToCsv(path, new RequestCsvExporter());

            ConsoleView.ok("Экспорт запросов завершён.");

        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Не удалось выполнить экспорт: " + e.getMessage());
        }
    }
}
