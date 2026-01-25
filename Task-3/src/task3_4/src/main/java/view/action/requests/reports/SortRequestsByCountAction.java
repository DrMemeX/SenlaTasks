package view.action.requests.reports;

import features.reports.RequestReportService;
import view.action.IAction;

public class SortRequestsByCountAction implements IAction {

    private final RequestReportService service;

    public SortRequestsByCountAction(RequestReportService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Сортировать запросы по количеству ожиданий";
    }

    @Override
    public void execute() {
        // пусть будет по возрастанию
        service.showRequestsSortedByCount(true);
    }
}
