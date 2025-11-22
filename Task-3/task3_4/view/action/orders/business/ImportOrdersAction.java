package task3_4.view.action.orders.business;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.features.orders.*;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

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
        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Не удалось выполнить импорт: " + e.getMessage());
        }
    }
}
