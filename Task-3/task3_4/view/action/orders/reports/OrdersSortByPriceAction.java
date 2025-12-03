package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;

public class OrdersSortByPriceAction implements IAction {

    private final OrderReportService reports;

    public OrdersSortByPriceAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Сортировать заказы по цене";
    }

    @Override
    public void execute() {
        reports.showOrdersSortedByPrice(true);
    }
}
