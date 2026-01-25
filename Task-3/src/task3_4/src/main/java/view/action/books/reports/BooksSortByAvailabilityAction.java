package view.action.books.reports;


import features.reports.BookReportService;
import view.action.IAction;

public class BooksSortByAvailabilityAction implements IAction {

    private final BookReportService report;

    public BooksSortByAvailabilityAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Список книг по наличию";
    }

    @Override
    public void execute() {
        report.showBooksSortedByAvailability(true);
    }
}
