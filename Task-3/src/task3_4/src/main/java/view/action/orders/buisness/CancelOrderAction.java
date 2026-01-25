package view.action.orders.buisness;

import features.orders.OrderService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class CancelOrderAction implements IAction {

    private final OrderService service;

    public CancelOrderAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Отменить заказ";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange("Введите ID заказа: ", 1, Integer.MAX_VALUE);

        service.cancelOrder(id);
        ConsoleView.ok("Заказ отменён.");
    }
}
