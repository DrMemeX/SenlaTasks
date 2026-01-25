package exceptions.ui;

public class EmptyInputException extends UserInputException {
    public EmptyInputException() {
        super("Пустой ввод недопустим.");
    }
}
