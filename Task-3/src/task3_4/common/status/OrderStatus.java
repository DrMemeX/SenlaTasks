package task3_4.common.status;

public enum OrderStatus {
    NEW("Новый"),
    COMPLETED("Выполнен"),
    CANCELLED("Отменён");

    private final String description;

    OrderStatus(String description) {
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
