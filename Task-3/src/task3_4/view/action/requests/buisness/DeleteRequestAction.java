package task3_4.view.action.requests.buisness;

import task3_4.features.requests.RequestService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class DeleteRequestAction implements IAction {

    private final RequestService service;

    public DeleteRequestAction(RequestService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Удалить запрос";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange(
                "Введите ID запроса: ",
                1,
                Integer.MAX_VALUE
        );

        service.deleteRequest(id);
        ConsoleView.ok("Запрос удалён.");
    }
}
