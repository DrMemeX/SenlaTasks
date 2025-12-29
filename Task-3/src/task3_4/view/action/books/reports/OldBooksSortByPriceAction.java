package task3_4.view.action.books.reports;


import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;

public class OldBooksSortByPriceAction implements IAction {

    private final BookReportService report;

    public OldBooksSortByPriceAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Залежавшиеся книги (по цене)";
    }

    @Override
    public void execute() {
        report.showOldBooks(false, true, true);
    }
}