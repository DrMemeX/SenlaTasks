package view.action.orders.reports;

import features.reports.OrderReportService;
import view.action.IAction;

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
