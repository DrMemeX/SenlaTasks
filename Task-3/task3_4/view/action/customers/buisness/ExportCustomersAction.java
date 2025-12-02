package task3_4.view.action.customers.buisness;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.cvs.exporter.CustomerCsvExporter;
import task3_4.features.customers.CustomerService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

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
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Не удалось выполнить экспорт: " + e.getMessage());
        }
    }
}
