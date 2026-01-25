package view.action.books.reports;

import features.reports.BookReportService;
import view.action.IAction;

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
