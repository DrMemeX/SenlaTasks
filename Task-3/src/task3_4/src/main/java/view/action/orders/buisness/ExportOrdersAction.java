package view.action.orders.buisness;

import cvs.exporter.OrderCsvExporter;
import exceptions.csv.CsvException;
import features.orders.OrderService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
