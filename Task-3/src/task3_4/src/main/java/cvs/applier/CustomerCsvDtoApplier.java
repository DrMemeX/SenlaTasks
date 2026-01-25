package cvs.applier;

import exceptions.csv.CsvMappingException;
import features.customers.Customer;
import features.customers.CustomerCsvDto;
import features.customers.CustomerRepository;

public class CustomerCsvDtoApplier extends AbstractCsvDtoApplier<CustomerCsvDto, Customer> {

    private final CustomerRepository repo;

    public CustomerCsvDtoApplier(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public Customer apply(CustomerCsvDto dto) {

        checkField(dto.name, new CsvMappingException(
                "Ошибка маппинга клиента ID=" + dto.id +
                        ": пустое или отсутствующее имя."
        ));

        checkField(dto.phone, new CsvMappingException(
                "Ошибка маппинга клиента ID=" + dto.id +
                        ": телефон отсутствует."
        ));

        checkField(dto.email, new CsvMappingException(
                "Ошибка маппинга клиента ID=" + dto.id +
                        ": email отсутствует."
        ));

        Customer existing = repo.findCustomerById(dto.id);

        if (existing == null) {

            Customer customer = new Customer(
                    dto.id,
                    dto.name,
                    dto.phone,
                    dto.email,
                    dto.address != null ? dto.address : ""
            );

            repo.addCustomer(customer);
            return customer;
        }

        existing.setName(dto.name);
        existing.setPhone(dto.phone);
        existing.setEmail(dto.email);
        existing.setAddress(dto.address != null ? dto.address : "");

        return existing;
    }
}
