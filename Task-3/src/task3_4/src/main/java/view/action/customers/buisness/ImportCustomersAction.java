package view.action.customers.buisness;

import cvs.applier.CustomerCsvDtoApplier;
import cvs.importer.CustomerCsvImporter;
import exceptions.csv.CsvException;
import features.customers.CustomerService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
