package view.action.requests.reports;

import features.reports.RequestReportService;
import view.action.IAction;

public class ShowResolvedRequestsAction implements IAction {

    private final RequestReportService service;

    public ShowResolvedRequestsAction(RequestReportService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Показать выполненные запросы";
    }

    @Override
    public void execute() {
        service.showResolvedRequests();
    }
}