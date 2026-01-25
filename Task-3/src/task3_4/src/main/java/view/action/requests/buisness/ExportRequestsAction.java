package view.action.requests.buisness;

import cvs.exporter.RequestCsvExporter;
import exceptions.csv.CsvException;
import features.requests.RequestService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
        }
    }
}
