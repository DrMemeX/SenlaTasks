package task3_4.features.books;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.csv.CsvFileNotFoundException;
import task3_4.exceptions.csv.CsvFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookCsvImporter {

    public List<BookCsvDto> importFrom(String filePath) {

        List<BookCsvDto> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                if (line.isBlank()) continue;

                String[] p = line.split(";");

                if (p.length < 7) {
                    throw new CsvFormatException(
                            "Некорректная строка (мало колонок) в файле " + filePath +
                                    " [строка " + row + "]"
                    );
                }

                BookCsvDto dto = new BookCsvDto();
                dto.id          = Long.parseLong(p[0]);
                dto.title       = p[1];
                dto.author      = p[2];
                dto.price       = Double.parseDouble(p[3]);
                dto.status      = p[4];
                dto.releaseDate = LocalDate.parse(p[5]);
                dto.addedDate   = LocalDate.parse(p[6]);
                dto.description = (p.length > 7 ? p[7] : "");

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