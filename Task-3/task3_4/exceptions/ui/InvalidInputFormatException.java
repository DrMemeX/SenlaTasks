package task3_4.exceptions.ui;

public class InvalidInputFormatException extends UserInputException {
    public InvalidInputFormatException(String input) {
        super("Некорректный ввод: \"" + input + "\".");
    }
}
