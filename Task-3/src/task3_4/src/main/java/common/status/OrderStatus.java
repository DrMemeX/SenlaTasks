package common.status;

public enum OrderStatus {
    NEW("Новый"),
    COMPLETED("Выполнен"),
    CANCELLED("Отменён");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String toDb() {
        return name();
    }

    public static OrderStatus fromDb(String value) {
        if (value == null) return null;
        return OrderStatus.valueOf(value.trim().toUpperCase());
    }

    @Override
    public String toString() {
        return description;
    }
}
