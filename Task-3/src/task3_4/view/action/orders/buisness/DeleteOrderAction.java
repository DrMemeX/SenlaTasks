package task3_4.view.action.orders.buisness;

import task3_4.features.orders.OrderService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class DeleteOrderAction implements IAction {

    private final OrderService service;

    public DeleteOrderAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Удалить заказ";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange("Введите ID заказа: ", 1, Integer.MAX_VALUE);

        service.deleteOrder(id);
        ConsoleView.ok("Заказ удалён.");
    }
}
