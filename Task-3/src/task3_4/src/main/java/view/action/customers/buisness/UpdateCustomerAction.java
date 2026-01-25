package view.action.customers.buisness;

import features.customers.Customer;
import features.customers.CustomerService;
import view.action.IAction;
import view.util.ConsoleView;
import view.util.In;

public class UpdateCustomerAction implements IAction {

    private final CustomerService service;

    public UpdateCustomerAction(CustomerService service) {
        this.service = service;
    }

    @Override
    public String title() {
        return "Изменить данные клиента";
    }

    @Override
    public void execute() {
        long id = In.get().intInRange(
                "Введите ID клиента: ",
                1,
                Integer.MAX_VALUE
        );

        Customer existing = service.getCustomerById(id);

        String name = In.get().line("Имя (" + existing.getName() + "): ");
        String phone = In.get().line("Телефон (" + existing.getPhone() + "): ");
        String email = In.get().line("Email (" + existing.getEmail() + "): ");
        String address = In.get().line("Адрес (" + existing.getAddress() + "): ");

        Customer updated = new Customer(
                id,
                name.isBlank() ? existing.getName() : name,
                phone.isBlank() ? existing.getPhone() : phone,
                email.isBlank() ? existing.getEmail() : email,
                address.isBlank() ? existing.getAddress() : address
        );

        service.updateCustomer(updated);
        ConsoleView.ok("Данные клиента обновлены.");
    }
}
