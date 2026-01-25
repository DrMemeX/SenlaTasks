package view.action.requests.buisness;

import features.requests.RequestService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class CompleteRequestAction implements IAction {

    private final RequestService service;

    public CompleteRequestAction(RequestService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Пометить запрос как выполненный";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange(
                "Введите ID запроса: ",
                1,
                Integer.MAX_VALUE
        );

        service.completeRequest(id);
        ConsoleView.ok("Запрос помечен как выполненный.");
    }
}
