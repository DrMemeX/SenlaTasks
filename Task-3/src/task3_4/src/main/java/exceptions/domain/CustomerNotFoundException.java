package exceptions.domain;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(long id) {
        super("Покупатель с ID=" + id + " не найден.");
    }
}
