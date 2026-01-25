package view.action.books.reports;


import features.reports.BookReportService;
import view.action.IAction;
import view.util.In;

public class BookDescriptionByTitleAndAuthorAction implements IAction {

    private final BookReportService report;

    public BookDescriptionByTitleAndAuthorAction(BookReportService report) {
        this.report = report;
    }

    @Override
    public String title() {
        return "Описание книги (название + автор)";
    }

    @Override
    public void execute() {
        String title = In.get().line("Введите название: ");
        String author = In.get().line("Введите автора: ");
        report.showBookDescription(title, author);
    }
}
