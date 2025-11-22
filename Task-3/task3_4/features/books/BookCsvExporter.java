package task3_4.features.books;

import task3_4.exceptions.csv.CsvException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class BookCsvExporter {

    public void exportTo(String filePath, List<Book> books) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Book b : books) {
                pw.printf("%d;%s;%s;%.2f;%s;%s;%s;%s%n",
                        b.getId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getPrice(),
                        b.getStatus(),
                        b.getReleaseDate(),
                        b.getAddedDate(),
                        b.getDescription()
                );
            }

        } catch (Exception e) {
            throw new CsvException(
                    "Ошибка экспорта книг в CSV: " + filePath
            );
        }
    }
}