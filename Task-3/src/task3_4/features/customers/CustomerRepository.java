package task3_4.features.customers;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.dao.jdbc.CustomerJdbcDao;

import java.util.List;

@Component
@Singleton
public class CustomerRepository {

    @Inject
    private CustomerJdbcDao dao;

    public List<Customer> findAllCustomers() {
        return dao.findAll();
    }

    public Customer findCustomerById(long id) {
        return dao.findById(id).orElse(null);
    }

    public Customer findCustomerByEmail(String email) {
        return dao.findByEmail(email).orElse(null);
    }

    public void addCustomer(Customer customer) {
        dao.create(customer);
    }

    public boolean deleteCustomerById(long id) {
        return dao.deleteById(id);
    }

    public void restoreState(List<Customer> customerState) {
    }

    public void updateCustomer(Customer customer) {
        dao.update(customer);
    }
}
