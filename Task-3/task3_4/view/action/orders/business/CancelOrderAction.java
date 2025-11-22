package task3_4.view.action.orders.business;

import task3_4.exceptions.domain.DomainException;
import task3_4.features.orders.OrderService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

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

        try {
            long id = In.get().intInRange(
                    "Введите ID заказа: ",
                    1,
                    Integer.MAX_VALUE
            );

            service.cancelOrder(id);

            ConsoleView.ok("Заказ отменён.");

        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Ошибка при отмене заказа: " + e.getMessage());
        }
    }
}
