package view.action.orders.reports;

import features.reports.OrderReportService;
import view.action.IAction;

public class OrdersSortByStatusAction implements IAction {

    private final OrderReportService reports;

    public OrdersSortByStatusAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Сортировать заказы по статусу";
    }

    @Override
    public void execute() {
        reports.showOrdersSortedByStatus(true);
    }
}
