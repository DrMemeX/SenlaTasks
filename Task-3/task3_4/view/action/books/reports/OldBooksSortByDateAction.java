package task3_4.view.action.books.reports;

import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;

public class OldBooksSortByDateAction implements IAction {

    private final BookReportService report;

    public OldBooksSortByDateAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Залежавшиеся книги (по дате поступления)";
    }

    @Override
    public void execute() {
        report.showOldBooks(true, false, true);
    }
}
