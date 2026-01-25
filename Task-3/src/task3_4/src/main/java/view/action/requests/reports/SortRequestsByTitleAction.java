package view.action.requests.reports;

import features.reports.RequestReportService;
import view.action.IAction;

public class SortRequestsByTitleAction implements IAction {

    private final RequestReportService service;

    public SortRequestsByTitleAction(RequestReportService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Сортировать запросы по алфавиту (книга)";
    }

    @Override
    public void execute() {
        // алфавит А → Я
        service.showRequestsSortedByTitle(true);
    }
}
