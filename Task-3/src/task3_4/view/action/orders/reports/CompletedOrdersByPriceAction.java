package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;
import task3_4.view.util.In;

import java.time.LocalDate;

public class CompletedOrdersByPriceAction implements IAction {

    private final OrderReportService reports;

    public CompletedOrdersByPriceAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Выполненные заказы за период (по цене)";
    }

    @Override
    public void execute() {
        LocalDate from = LocalDate.parse(In.get().line("Дата начала (ГГГГ-ММ-ДД): "));
        LocalDate to   = LocalDate.parse(In.get().line("Дата окончания (ГГГГ-ММ-ДД): "));

        reports.showCompletedOrdersByPrice(from, to, true);
    }
}
