package task3_4.features.customers;

import task3_4.exceptions.csv.CsvMappingException;

public class CustomerCsvDtoApplier {

    private final CustomerRepository repo;

    public CustomerCsvDtoApplier(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer apply(CustomerCsvDto dto) {

        if (dto.name == null || dto.name.isBlank()) {
            throw new CsvMappingException(
                    "Ошибка маппинга клиента ID=" + dto.id +
                            ": пустое или отсутствующее имя."
            );
        }

        if (dto.phone == null || dto.phone.isBlank()) {
            throw new CsvMappingException(
                    "Ошибка маппинга клиента ID=" + dto.id +
                            ": телефон отсутствует."
            );
        }

        if (dto.email == null || dto.email.isBlank()) {
            throw new CsvMappingException(
                    "Ошибка маппинга клиента ID=" + dto.id +
                            ": email отсутствует."
            );
        }

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
