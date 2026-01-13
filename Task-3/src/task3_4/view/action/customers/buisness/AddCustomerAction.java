package task3_4.view.action.customers.buisness;

import task3_4.exceptions.domain.DomainException;
import task3_4.features.customers.Customer;
import task3_4.features.customers.CustomerService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

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
