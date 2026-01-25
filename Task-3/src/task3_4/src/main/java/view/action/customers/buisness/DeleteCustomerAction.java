package view.action.customers.buisness;

import features.customers.CustomerService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

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
