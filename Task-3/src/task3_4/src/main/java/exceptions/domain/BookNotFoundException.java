package exceptions.domain;

public class BookNotFoundException extends DomainException {

    public BookNotFoundException(long id) {
        super("Книга с ID=" + id + " не найдена.");
    }
}
