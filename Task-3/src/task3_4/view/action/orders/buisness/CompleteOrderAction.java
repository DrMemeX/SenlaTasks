package task3_4.view.action.orders.buisness;

import task3_4.features.orders.OrderService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class CompleteOrderAction implements IAction {

    private final OrderService service;

    public CompleteOrderAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Завершить заказ";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange("Введите ID заказа: ", 1, Integer.MAX_VALUE);

        service.cancelOrder(id);
        ConsoleView.ok("Заказ отменён.");
    }
}
