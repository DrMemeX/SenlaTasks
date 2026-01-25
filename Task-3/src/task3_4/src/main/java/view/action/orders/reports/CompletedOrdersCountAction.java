package view.action.orders.reports;


import features.reports.OrderReportService;
import view.action.IAction;
import view.util.In;

import java.time.LocalDate;

public class CompletedOrdersCountAction implements IAction {

    private final OrderReportService reports;

    public CompletedOrdersCountAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Количество выполненных заказов за период";
    }

    @Override
    public void execute() {
        LocalDate from = LocalDate.parse(In.get().line("Дата начала: "));
        LocalDate to   = LocalDate.parse(In.get().line("Дата окончания: "));

        reports.showCompletedOrdersCount(from, to);
    }
}
