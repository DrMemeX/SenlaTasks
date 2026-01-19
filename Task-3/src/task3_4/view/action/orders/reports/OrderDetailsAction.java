package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;
import task3_4.view.util.In;

public class OrderDetailsAction implements IAction {

    private final OrderReportService reports;

    public OrderDetailsAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Посмотреть детали заказа";
    }

    @Override
    public void execute() {
        long id = Long.parseLong(In.get().line("Введите ID заказа: "));
        reports.showOrderDetails(id);
    }
}
