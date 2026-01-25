package view.action.customers.buisness;

import cvs.exporter.CustomerCsvExporter;
import exceptions.csv.CsvException;
import features.customers.CustomerService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class ExportCustomersAction implements IAction {

    private final CustomerService service;

    public ExportCustomersAction(CustomerService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Экспорт клиентов (CSV)";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь для сохранения CSV: ");

            service.exportCustomersToCsv(
                    path,
                    new CustomerCsvExporter()
            );

            ConsoleView.ok("Экспорт завершён успешно.");
        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        }
    }
}
