package task3_4.exceptions.csv;

public class CsvFileNotFoundException extends CsvException {
    public CsvFileNotFoundException(String path) {
        super("CSV файл не найден: " + path);
    }
}
