package features.orders;

import common.status.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public class OrderCsvDto {
    public long id;
    public long customerId;
    public OrderStatus status;
    public LocalDate creationDate;
    public LocalDate completionDate;
    public double totalPrice;
    public List<Long> bookIds;
}
