package task3_4.exceptions.domain;

public class RequestNotFoundException extends DomainException {

    public RequestNotFoundException(long id) {
        super("Запрос с ID=" + id + " не найден.");
    }
}