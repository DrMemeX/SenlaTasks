package cvs.importer;

import features.customers.CustomerCsvDto;

public class CustomerCsvImporter extends AbstractCsvImporter<CustomerCsvDto> {

    @Override
    protected int minColumns() {
        return 4;
    }

    @Override
    protected CustomerCsvDto parseLine(String[] p, int row) {

        CustomerCsvDto dto = new CustomerCsvDto();

        dto.id      = Long.parseLong(p[0]);
        dto.name    = p[1];
        dto.phone   = p[2];
        dto.email   = p[3];
        dto.address = (p.length > 4 ? p[4] : "");

        return dto;
    }

    @Override
    protected String buildReadError(String filePath) {
        return "Ошибка чтения CSV клиентов: " + filePath;
    }
}
