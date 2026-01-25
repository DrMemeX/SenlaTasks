package cvs.exporter;

import features.customers.Customer;

public class CustomerCsvExporter extends AbstractCsvExporter<Customer> {

    @Override
    protected String format(Customer c) {
        return String.format("%d;%s;%s;%s;%s",
                c.getId(),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                c.getAddress()
        );
    }

    @Override
    protected String buildErrorMessage(String filePath) {
        return "Ошибка экспорта клиентов в CSV: " + filePath;
    }
}
