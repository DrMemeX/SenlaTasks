package cvs.exporter;

import features.books.Book;

public class BookCsvExporter extends AbstractCsvExporter<Book> {

    @Override
    protected String format(Book b) {
        return String.format("%d;%s;%s;%.2f;%s;%s;%s;%s",
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

    @Override
    protected String buildErrorMessage(String filePath) {
        return "Ошибка экспорта книг в CSV: " + filePath;
    }
}
