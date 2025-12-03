package task3_4.cvs.importer;

import task3_4.exceptions.csv.CsvFormatException;
import task3_4.features.requests.RequestCsvDto;

import java.util.ArrayList;
import java.util.List;

public class RequestCsvImporter extends AbstractCsvImporter<RequestCsvDto> {

    @Override
    protected int minColumns() {
        return 3;
    }

    @Override
    protected RequestCsvDto parseLine(String[] p, int row) {

        try {
            RequestCsvDto dto = new RequestCsvDto();

            dto.id       = Long.parseLong(p[0]);
            dto.bookId   = Long.parseLong(p[1]);
            dto.resolved = Boolean.parseBoolean(p[2]);
            dto.waitingOrderIds = parseOrderIds(p.length > 3 ? p[3] : "");

            return dto;

        } catch (Exception ex) {
            throw new CsvFormatException(
                    "Ошибка парсинга строки " + row + ": " + ex.getMessage()
            );
        }
    }

    @Override
    protected String buildReadError(String filePath) {
        return "Ошибка чтения CSV запросов: " + filePath;
    }

    private List<Long> parseOrderIds(String raw) {

        List<Long> ids = new ArrayList<>();

        if (raw == null || raw.isBlank()) return ids;

        for (String part : raw.split(",")) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) continue;

            try {
                ids.add(Long.parseLong(trimmed));
            } catch (NumberFormatException e) {
                throw new CsvFormatException("Некорректный ID заказа: " + trimmed);
            }
        }

        return ids;
    }
}
