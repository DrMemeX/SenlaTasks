package view.action.orders.reports;

import features.reports.OrderReportService;
import view.action.IAction;

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
