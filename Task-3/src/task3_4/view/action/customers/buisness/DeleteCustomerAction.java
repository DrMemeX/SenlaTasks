package task3_4.view.action.customers.buisness;

import task3_4.features.customers.CustomerService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

public class DeleteCustomerAction implements IAction {

    private final CustomerService service;

    public DeleteCustomerAction(CustomerService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Удалить клиента";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange(
                "Введите ID клиента: ",
                1,
                Integer.MAX_VALUE
        );

        service.deleteCustomer(id);
        ConsoleView.ok("Клиент успешно удалён.");
    }
}
