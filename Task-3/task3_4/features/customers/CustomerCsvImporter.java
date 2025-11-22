package task3_4.features.customers;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.csv.CsvFileNotFoundException;
import task3_4.exceptions.csv.CsvFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CustomerCsvImporter {

    public List<CustomerCsvDto> importFrom(String filePath) {

        List<CustomerCsvDto> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                if (line.isBlank()) continue;

                String[] p = line.split(";");

                if (p.length < 4) {
                    throw new CsvFormatException(
                            "Некорректная строка в файле " + filePath +
                                    " [строка " + row + "]: недостаточно колонок"
                    );
                }

                CustomerCsvDto dto = new CustomerCsvDto();
                dto.id      = Long.parseLong(p[0]);
                dto.name    = p[1];
                dto.phone   = p[2];
                dto.email   = p[3];
                dto.address = (p.length > 4 ? p[4] : "");

                result.add(dto);
            }

        } catch (java.io.FileNotFoundException e) {

            throw new CsvFileNotFoundException(filePath);

        } catch (Exception e) {

            throw new CsvFormatException(
                    "Ошибка чтения или парсинга CSV файла: " + filePath
            );
        }

        return result;
    }
}