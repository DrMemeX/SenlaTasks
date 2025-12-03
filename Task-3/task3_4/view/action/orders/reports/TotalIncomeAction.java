package task3_4.view.action.orders.reports;

import task3_4.features.reports.OrderReportService;
import task3_4.view.action.IAction;
import task3_4.view.util.In;

import java.time.LocalDate;

public class TotalIncomeAction implements IAction {

    private final OrderReportService reports;

    public TotalIncomeAction(OrderReportService reports) {
        this.reports = reports;
    }

    @Override
    public String title() {
        return "Сумма заработанных средств за период";
    }

    @Override
    public void execute() {
        LocalDate from = LocalDate.parse(In.get().line("Дата начала: "));
        LocalDate to   = LocalDate.parse(In.get().line("Дата окончания: "));

        reports.showTotalIncome(from, to);
    }
}
