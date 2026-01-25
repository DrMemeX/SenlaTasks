package exceptions.ui;

public class InvalidMenuChoiceException extends UserInputException {
    public InvalidMenuChoiceException(long choice) {
        super("Пункт меню " + choice + " недоступен.");
    }
}