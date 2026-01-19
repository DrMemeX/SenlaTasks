package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;

public class OrdersSortByDateAction implements IAction {

    private final OrderReportService reports;

    public OrdersSortByDateAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Сортировать заказы по дате исполнения";
    }

    @Override
    public void execute() {
        reports.showOrdersSortedByCompletionDate(true);
    }
}
