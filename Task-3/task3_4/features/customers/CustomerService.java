package task3_4.features.customers;

import task3_4.exceptions.domain.CustomerNotFoundException;
import task3_4.exceptions.domain.DomainException;

import java.util.List;

public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public CustomerRepository getRepo() {
        return repo;
    }

    public List<Customer> getAllCustomers() {
        return repo.findAllCustomers();
    }

    public Customer getCustomerById(long id) {
        Customer customer = repo.findCustomerById(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = repo.findCustomerByEmail(email);
        if (customer == null) {
            throw new DomainException(
                    "Клиент с email \"" + email + "\" не найден."
            );
        }
        return customer;
    }

    public void addCustomer(Customer customer) {

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new DomainException("Email клиента обязателен.");
        }
        if (customer.getName() == null || customer.getName().isBlank()) {
            throw new DomainException("Имя клиента обязательно.");
        }

        repo.addCustomer(customer);
    }

    public void deleteCustomer(long id) {
        boolean removed = repo.deleteCustomerById(id);
        if (!removed) {
            throw new CustomerNotFoundException(id);
        }
    }

    public void updateCustomer(Customer incoming) {
        Customer existing = repo.findCustomerById(incoming.getId());

        if (existing == null) {
            throw new CustomerNotFoundException(incoming.getId());
        }

        if (incoming.getEmail() == null || incoming.getEmail().isBlank()) {
            throw new DomainException("Email клиента обязателен.");
        }
        if (incoming.getName() == null || incoming.getName().isBlank()) {
            throw new DomainException("Имя клиента обязательно.");
        }

        existing.setName(incoming.getName());
        existing.setPhone(incoming.getPhone());
        existing.setEmail(incoming.getEmail());
        existing.setAddress(incoming.getAddress());
    }

    public void importCustomersFromCsv(String filePath,
                                       CustomerCsvImporter importer,
                                       CustomerCsvDtoApplier applier) {

        List<CustomerCsvDto> dtos = importer.importFrom(filePath);
        for (CustomerCsvDto dto : dtos) {
            applier.apply(dto);
        }
    }

    public void exportCustomersToCsv(String filePath,
                                     CustomerCsvExporter exporter) {
        exporter.exportTo(filePath, repo.findAllCustomers());
    }
}
