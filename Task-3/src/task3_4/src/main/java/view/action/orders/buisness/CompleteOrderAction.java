package view.action.orders.buisness;

import features.orders.OrderService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
