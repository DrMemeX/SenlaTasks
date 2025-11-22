package task3_4.common.status;

public enum BookStatus {
    AVAILABLE("В наличии"),
    MISSING("Отсутствует");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
