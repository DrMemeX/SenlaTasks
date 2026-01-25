package view.action.orders.reports;


import features.reports.OrderReportService;
import view.action.IAction;
import view.util.In;

import java.time.LocalDate;

public class CompletedOrdersByDateAction implements IAction {

    private final OrderReportService reports;

    public CompletedOrdersByDateAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Выполненные заказы за период (по дате)";
    }

    @Override
    public void execute() {
        LocalDate from = LocalDate.parse(In.get().line("Дата начала (ГГГГ-ММ-ДД): "));
        LocalDate to   = LocalDate.parse(In.get().line("Дата окончания (ГГГГ-ММ-ДД): "));

        reports.showCompletedOrdersByDate(from, to, true);
    }
}