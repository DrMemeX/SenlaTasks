package task3_4.exceptions.ui;

public class InvalidMenuChoiceException extends UserInputException {
    public InvalidMenuChoiceException(int choice) {
        super("Пункт меню " + choice + " недоступен.");
    }
}