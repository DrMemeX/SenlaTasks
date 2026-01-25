package view.action.orders.buisness;

import common.status.OrderStatus;
import features.orders.Order;
import features.orders.OrderService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class UpdateOrderStatusAction implements IAction {

    private final OrderService service;

    public UpdateOrderStatusAction(OrderService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Изменить статус заказа";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange("Введите ID заказа: ", 1, Integer.MAX_VALUE);

        Order order = service.getOrderById(id);

        ConsoleView.title("Выбор статуса");
        ConsoleView.info("1) NEW");
        ConsoleView.info("2) COMPLETED");
        ConsoleView.info("3) CANCELLED");

        int c = In.get().intInRange("Выберите статус: ", 1, 3);

        OrderStatus status = switch (c) {
            case 1 -> OrderStatus.NEW;
            case 2 -> OrderStatus.COMPLETED;
            default -> OrderStatus.CANCELLED;
        };

        order.setStatus(status);
        service.updateOrder(order);

        ConsoleView.ok("Статус заказа обновлён.");
    }
}
