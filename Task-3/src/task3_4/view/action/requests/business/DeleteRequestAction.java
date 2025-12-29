package task3_4.view.action.requests.business;

import task3_4.exceptions.domain.DomainException;
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
        try {
            long id = In.get().intInRange(
                    "Введите ID запроса: ",
                    1,
                    Integer.MAX_VALUE
            );

            service.deleteRequest(id);

            ConsoleView.ok("Запрос удалён.");

        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Ошибка при удалении запроса: " + e.getMessage());
        }
    }
}
