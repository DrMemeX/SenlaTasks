package view.action.customers.buisness;

import features.customers.Customer;
import features.customers.CustomerService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class AddCustomerAction implements IAction {

    private final CustomerService service;

    public AddCustomerAction(CustomerService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Добавить клиента";
    }

    @Override
    public void execute() {
        String name = In.get().line("Введите имя: ");
        String phone = In.get().line("Введите телефон: ");
        String email = In.get().line("Введите email: ");
        String address = In.get().line("Введите адрес: ");

        Customer c = new Customer(
                0,
                name,
                phone,
                email,
                address
        );

        service.addCustomer(c);
        ConsoleView.ok("Клиент успешно добавлен!");
    }
}
