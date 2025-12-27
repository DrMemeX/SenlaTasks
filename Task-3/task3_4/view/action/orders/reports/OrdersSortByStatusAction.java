package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;

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
