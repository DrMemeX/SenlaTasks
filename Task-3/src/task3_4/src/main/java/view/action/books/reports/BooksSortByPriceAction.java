package view.action.books.reports;

import features.reports.BookReportService;
import view.action.IAction;

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
