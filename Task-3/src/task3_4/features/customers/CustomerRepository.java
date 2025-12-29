package task3_4.features.customers;

import di_module.di_annotation.Component;
import di_module.di_annotation.Singleton;

import java.util.ArrayList;
import java.util.List;

@Component
@Singleton
public class CustomerRepository {

    private final List<Customer> customerList = new ArrayList<>();

    public List<Customer> findAllCustomers() {
        return customerList;
    }

    public Customer findCustomerById(long id) {
        return customerList.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Customer findCustomerByEmail(String email) {
        return customerList.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    public boolean deleteCustomerById(long id) {
        return customerList.removeIf(c -> c.getId() == id);
    }

    public void restoreState(List<Customer> customerState) {
        if (customerState != null) {
            customerList.clear();
            customerList.addAll(customerState);
        }
    }
}
