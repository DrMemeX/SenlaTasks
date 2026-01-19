package task3_4.exceptions.domain;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(long id) {
        super("Покупатель с ID=" + id + " не найден.");
    }
}
