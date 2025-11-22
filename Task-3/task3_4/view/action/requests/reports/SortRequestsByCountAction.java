package task3_4.view.action.requests.reports;

import task3_4.features.reports.RequestReportService;
import task3_4.view.action.IAction;

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
