package task3_4.common.status;

public enum BookStatus {
    AVAILABLE("В наличии"),
    MISSING("Отсутствует");

    private final String description;

    BookStatus(String description) {
        this.description = description;
    }

    public String toDb() {
        return name();
    }

    public static BookStatus fromDb(String value) {
        if (value == null) return null;
        return BookStatus.valueOf(value.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return description;
    }
}
