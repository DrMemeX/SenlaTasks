package task3_4.view.action.books.reports;

import task3_4.features.reports.BookReportService;
import task3_4.view.action.IAction;
import task3_4.view.util.In;

public class BookDescriptionByTitleAction implements IAction {

    private final BookReportService report;

    public BookDescriptionByTitleAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Описание книги (по названию)";
    }

    @Override
    public void execute() {
        String title = In.get().line("Введите название книги: ");
        report.showBookDescription(title);
    }
}
