package task3_4.features.requests;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.csv.CsvFileNotFoundException;
import task3_4.exceptions.csv.CsvFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RequestCsvImporter {

    public List<RequestCsvDto> importFrom(String filePath) {

        List<RequestCsvDto> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                if (line.isBlank()) continue;

                String[] p = line.split(";");

                if (p.length < 3) {
                    throw new CsvFormatException(
                            "Некорректная строка в файле '" + filePath +
                                    "' [строка " + row + "]: ожидалось минимум 3 колонки, получено " + p.length
                    );
                }

                RequestCsvDto dto = new RequestCsvDto();

                try {
                    dto.id       = Long.parseLong(p[0]);
                    dto.bookId   = Long.parseLong(p[1]);
                    dto.resolved = Boolean.parseBoolean(p[2]);

                    dto.waitingOrderIds = parseOrderIds(
                            p.length > 3 ? p[3] : ""
                    );

                } catch (Exception parseEx) {
                    throw new CsvFormatException(
                            "Ошибка парсинга строки " + row + " в файле '" + filePath +
                                    "': " + parseEx.getMessage()
                    );
                }

                result.add(dto);
            }

        } catch (java.io.FileNotFoundException e) {

            throw new CsvFileNotFoundException(filePath);

        } catch (CsvFormatException e) {

            throw e;

        } catch (Exception e) {

            throw new CsvException(
                    "Ошибка чтения CSV файла: " + filePath,
                    e
            );
        }

        return result;
    }

    private List<Long> parseOrderIds(String raw) {

        List<Long> ids = new ArrayList<>();

        if (raw == null || raw.isBlank()) return ids;

        String[] parts = raw.split(",");

        for (String part : parts) {
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
