package task3_4.view.action.requests.reports;

import task3_4.features.reports.RequestReportService;
import task3_4.view.action.IAction;

public class ShowUnresolvedRequestsAction implements IAction {

    private final RequestReportService service;

    public ShowUnresolvedRequestsAction(RequestReportService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Показать невыполненные запросы";
    }

    @Override
    public void execute() {
        service.showUnresolvedRequests();
    }
}
