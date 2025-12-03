package task3_4.cvs.applier;

import task3_4.common.status.BookStatus;
import task3_4.exceptions.csv.CsvMappingException;
import task3_4.features.books.Book;
import task3_4.features.books.BookCsvDto;
import task3_4.features.books.BookRepository;

public class BookCsvDtoApplier extends AbstractCsvDtoApplier<BookCsvDto, Book> {

    private final BookRepository bookRepo;

    public BookCsvDtoApplier(BookRepository repo) {
        this.bookRepo = repo;
    }

    @Override
    public Book apply(BookCsvDto dto) {

        checkField(dto.title, new CsvMappingException(
                "Ошибка маппинга книги ID=" + dto.id +
                        ": пустой или отсутствующий title."
        ));

        checkField(dto.author, new CsvMappingException(
                "Ошибка маппинга книги ID=" + dto.id +
                        ": пустой или отсутствующий author."
        ));

        if (dto.price < 0) {
            throw new CsvMappingException(
                    "Ошибка маппинга книги ID=" + dto.id +
                            ": цена не может быть отрицательной."
            );
        }

        BookStatus status;
        try {
            status = BookStatus.valueOf(dto.status);
        } catch (Exception e) {
            throw new CsvMappingException(
                    "Ошибка маппинга книги ID=" + dto.id +
                            ": недопустимый статус \"" + dto.status + "\"."
            );
        }

        if (dto.releaseDate == null) {
            throw new CsvMappingException(
                    "Ошибка маппинга книги ID=" + dto.id +
                            ": отсутствует дата издания книги."
            );
        }

        if (dto.addedDate == null) {
            throw new CsvMappingException(
                    "Ошибка маппинга книги ID=" + dto.id +
                            ": отсутствует дата добавления книги."
            );
        }

        if (dto.addedDate.isBefore(dto.releaseDate)) {
            throw new CsvMappingException(
                    "Ошибка маппинга книги ID=" + dto.id +
                            ": дата добавления книги не может быть раньше даты издания."
            );
        }

        String description = (dto.description != null ? dto.description : "");

        Book existing = bookRepo.findBookById(dto.id);

        if (existing == null) {

            Book book = new Book(
                    dto.id,
                    dto.title,
                    dto.author,
                    dto.price,
                    status,
                    dto.releaseDate,
                    dto.addedDate,
                    description
            );

            bookRepo.addBook(book);
            return book;
        }

        existing.setTitle(dto.title);
        existing.setAuthor(dto.author);
        existing.setPrice(dto.price);
        existing.setStatus(status);
        existing.setReleaseDate(dto.releaseDate);
        existing.setAddedDate(dto.addedDate);
        existing.setDescription(description);

        return existing;
    }
}
