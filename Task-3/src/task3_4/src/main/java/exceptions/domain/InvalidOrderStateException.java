package exceptions.domain;

public class InvalidOrderStateException extends DomainException {

    public InvalidOrderStateException(long orderId, String message) {
        super("Неверное состояние заказа ID=" + orderId + ": " + message);
    }
}