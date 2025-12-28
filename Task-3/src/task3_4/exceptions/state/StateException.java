package task3_4.exceptions.state;

public class StateException extends RuntimeException {
    public StateException(String message) {
        super(message);
    }

    public StateException(String message, Throwable cause) {
        super(message, cause);
    }
}
