package exceptions.ui;

public class InvalidInputFormatException extends UserInputException {
    public InvalidInputFormatException(String input) {
        super("Некорректный ввод: \"" + input + "\".");
    }
}
