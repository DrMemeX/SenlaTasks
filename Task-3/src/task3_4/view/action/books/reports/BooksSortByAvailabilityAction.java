package task3_4.view.action.books.reports;


import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;

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
