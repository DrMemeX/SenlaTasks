package view.action.orders.buisness;

import cvs.applier.OrderCsvDtoApplier;
import cvs.importer.OrderCsvImporter;
import exceptions.csv.CsvException;
import features.orders.OrderService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class ImportOrdersAction implements IAction {

    private final OrderService service;

    public ImportOrdersAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Импорт заказов (CSV)";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь к CSV: ");

            service.importOrdersFromCsv(
                    path,
                    new OrderCsvImporter(),
                    new OrderCsvDtoApplier(
                            service.getOrderRepository(),
                            service.getCustomerService(),
                            service.getBookService()
                    )
            );

            ConsoleView.ok("Импорт завершён.");
        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        }
    }
}
