package task3_4.view.action.customers.buisness;

import task3_4.exceptions.csv.CsvException;
import task3_4.cvs.applier.CustomerCsvDtoApplier;
import task3_4.cvs.importer.CustomerCsvImporter;
import task3_4.features.customers.CustomerService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class ImportCustomersAction implements IAction {

    private final CustomerService service;

    public ImportCustomersAction(CustomerService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Импорт клиентов (CSV)";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь к CSV файлу: ");

            service.importCustomersFromCsv(
                    path,
                    new CustomerCsvImporter(),
                    new CustomerCsvDtoApplier(service.getRepo())
            );

            ConsoleView.ok("Импорт успешно завершён.");

        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        }
    }
}
