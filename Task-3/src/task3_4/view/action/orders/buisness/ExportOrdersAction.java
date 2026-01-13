package task3_4.view.action.orders.buisness;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.domain.DomainException;
import task3_4.cvs.exporter.OrderCsvExporter;
import task3_4.features.orders.OrderService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class ExportOrdersAction implements IAction {

    private final OrderService service;

    public ExportOrdersAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Экспорт заказов в CSV";
    }

    @Override
    public void execute() {

        try {
            String path = In.get().line("Введите путь сохранения CSV: ");

            service.exportOrdersToCsv(path, new OrderCsvExporter());

            ConsoleView.ok("Экспорт выполнен.");

        } catch (CsvException e) {
            ConsoleView.warn("Ошибка CSV: " + e.getMessage());
        }
    }
}
