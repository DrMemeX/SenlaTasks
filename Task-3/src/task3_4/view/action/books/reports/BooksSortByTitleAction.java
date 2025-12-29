package task3_4.view.action.books.reports;

import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;

public class BooksSortByTitleAction implements IAction {

    private final BookReportService report;

    public BooksSortByTitleAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Список книг по алфавиту";
    }

    @Override
    public void execute() {
        report.showBooksSortedByTitle(true);
    }
}
