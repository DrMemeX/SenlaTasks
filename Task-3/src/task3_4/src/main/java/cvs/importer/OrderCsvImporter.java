package cvs.importer;

import common.status.OrderStatus;
import exceptions.csv.CsvFormatException;
import features.orders.OrderCsvDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderCsvImporter extends AbstractCsvImporter<OrderCsvDto> {

    @Override
    protected int minColumns() {
        return 6;
    }

    @Override
    protected OrderCsvDto parseLine(String[] p, int row) {

        try {
            OrderCsvDto dto = new OrderCsvDto();

            dto.id          = Long.parseLong(p[0]);
            dto.customerId  = Long.parseLong(p[1]);
            dto.status      = OrderStatus.valueOf(p[2]);
            dto.creationDate = LocalDate.parse(p[3]);
            dto.completionDate =
                    "-".equals(p[4]) ? null : LocalDate.parse(p[4]);
            dto.totalPrice = Double.parseDouble(p[5]);

            dto.bookIds = parseBookIds(p.length > 6 ? p[6] : "");

            return dto;
        } catch (Exception ex) {
            throw new CsvFormatException("Ошибка парсинга строки " + row + ": " + ex.getMessage());
        }
    }

    @Override
    protected String buildReadError(String filePath) {
        return "Ошибка чтения CSV заказов: " + filePath;
    }

    private List<Long> parseBookIds(String raw) {

        List<Long> ids = new ArrayList<>();

        if (raw == null || raw.isBlank()) return ids;

        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) continue;

            try {
                ids.add(Long.parseLong(trimmed));
            } catch (NumberFormatException e) {
                throw new CsvFormatException("Некорректный ID книги: " + trimmed);
            }
        }

        return ids;
    }
}
