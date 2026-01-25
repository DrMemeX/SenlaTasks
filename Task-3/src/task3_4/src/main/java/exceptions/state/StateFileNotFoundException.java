package exceptions.state;

public class StateFileNotFoundException extends StateException {

    public StateFileNotFoundException(String filePath) {
        super("Файл состояния не найден: " + filePath);
    }
}
