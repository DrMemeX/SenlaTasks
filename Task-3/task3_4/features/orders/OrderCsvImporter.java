package task3_4.features.orders;

import task3_4.common.status.OrderStatus;
import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.csv.CsvFileNotFoundException;
import task3_4.exceptions.csv.CsvFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderCsvImporter {

    public List<OrderCsvDto> importFrom(String filePath) {

        List<OrderCsvDto> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                if (line.isBlank()) continue;

                String[] p = line.split(";");

                if (p.length < 6) {
                    throw new CsvFormatException(
                            "Некорректная строка в файле " + filePath +
                                    " [строка " + row + "]: ожидается минимум 6 колонок, получено " + p.length
                    );
                }

                OrderCsvDto dto = new OrderCsvDto();

                try {
                    dto.id          = Long.parseLong(p[0]);
                    dto.customerId  = Long.parseLong(p[1]);
                    dto.status      = OrderStatus.valueOf(p[2]);

                    dto.creationDate = LocalDate.parse(p[3]);

                    dto.completionDate =
                            "-".equals(p[4]) ? null : LocalDate.parse(p[4]);

                    dto.totalPrice = Double.parseDouble(p[5]);

                    dto.bookIds = parseBookIds(p.length > 6 ? p[6] : "");

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

            throw new CsvException("Ошибка чтения файла CSV: " + filePath, e);
        }

        return result;
    }

    private List<Long> parseBookIds(String value) {

        List<Long> ids = new ArrayList<>();

        if (value == null || value.isBlank()) return ids;

        String[] parts = value.split(",");

        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) continue;

            try {
                ids.add(Long.parseLong(trimmed));
            } catch (NumberFormatException ex) {
                throw new CsvFormatException("Некорректный ID книги: " + trimmed);
            }
        }

        return ids;
    }
}
