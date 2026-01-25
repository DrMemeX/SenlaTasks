package view.action.orders.reports;

import features.reports.OrderReportService;
import view.action.IAction;
import view.util.In;

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
