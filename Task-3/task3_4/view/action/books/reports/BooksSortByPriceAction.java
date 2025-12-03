package task3_4.view.action.books.reports;

import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;

public class BooksSortByPriceAction implements IAction {

    private final BookReportService report;

    public BooksSortByPriceAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Список книг по цене";
    }

    @Override
    public void execute() {
        report.showBooksSortedByPrice(true);
    }
}
