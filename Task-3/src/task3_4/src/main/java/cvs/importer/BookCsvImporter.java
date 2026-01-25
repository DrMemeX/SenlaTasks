package cvs.importer;

import features.books.BookCsvDto;

import java.time.LocalDate;

public class BookCsvImporter extends AbstractCsvImporter<BookCsvDto> {

    @Override
    protected int minColumns() {
        return 7;
    }

    @Override
    protected BookCsvDto parseLine(String[] p, int row) {

        BookCsvDto dto = new BookCsvDto();

        dto.id          = Long.parseLong(p[0]);
        dto.title       = p[1];
        dto.author      = p[2];
        dto.price       = Double.parseDouble(p[3]);
        dto.status      = p[4];
        dto.releaseDate = LocalDate.parse(p[5]);
        dto.addedDate   = LocalDate.parse(p[6]);
        dto.description = (p.length > 7 ? p[7] : "");

        return dto;
    }

    @Override
    protected String buildReadError(String filePath) {
        return "Ошибка чтения или парсинга CSV файла книг: " + filePath;
    }
}
