package view.action.books.reports;

import features.reports.BookReportService;
import view.action.IAction;

public class BooksSortByReleaseDateAction implements IAction {

    private final BookReportService report;

    public BooksSortByReleaseDateAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Список книг по дате выпуска";
    }

    @Override
    public void execute() {
        report.showBooksSortedByReleaseDate(true);
    }
}