package exceptions.domain;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(long id) {
        super("Заказ с ID=" + id + " не найден.");
    }
}
