package task3_4.exceptions.app;

public class AppInitializationException extends RuntimeException {

    public AppInitializationException(String message) {
        super(message);
    }

    public AppInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
