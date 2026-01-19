package task3_4.features.orders;

import task3_4.features.books.Book;
import task3_4.features.customers.Customer;
import task3_4.common.status.OrderStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 123L;

    private long id;
    private List<Book> orderedBooks;
    private OrderStatus status;
    private LocalDate creationDate;
    private LocalDate completionDate;
    private double totalPrice;

    private long customerId;
    private Customer customer;

    public Order() {
        this.orderedBooks = new ArrayList<>();
        this.status = OrderStatus.NEW;
        this.creationDate = LocalDate.now();
        this.completionDate = null;
        this.totalPrice = 0.0;
    }

    public Order(long id) {
        this.id = id;
        this.orderedBooks = new ArrayList<>();
        this.status = OrderStatus.NEW;
        this.creationDate = LocalDate.now();
        this.completionDate = null;
        this.totalPrice = 0.0;
    }

    public long getId() { return id; }
    public List<Book> getOrderedBooks() { return orderedBooks; }
    public OrderStatus getStatus() { return status; }
    public LocalDate getCreationDate() { return creationDate; }
    public LocalDate getCompletionDate() { return completionDate; }
    public double getTotalPrice() { return totalPrice; }

    public long getCustomerId() {
        if (customer != null) return customer.getId();
        return customerId;
    }
    public Customer getCustomer() { return customer; }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == 0;
    }

    public void setOrderedBooks(List<Book> orderedBooks) { this.orderedBooks = orderedBooks; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public void setCreationDate(LocalDate creationDate) {this.creationDate = creationDate;}
    public void setCompletionDate(LocalDate date) { this.completionDate = date; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }
    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerId = customer.getId();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Заказ №").append(id).append("\n");
        sb.append("Дата: ").append(completionDate != null ? completionDate : "—").append("\n");
        sb.append("Статус: ").append(status).append("\n");
        sb.append("Клиент ID: ").append(getCustomerId()).append("\n");
        sb.append("Состав заказа:\n");
        for (Book book : orderedBooks) {
            sb.append(" — ").append(book.getTitle())
                    .append(" — ").append(book.getAuthor())
                    .append(" (").append(book.getPrice()).append(" руб.)")
                    .append(" [").append(book.getStatus()).append("]\n");
        }
        sb.append("Итого: ").append(totalPrice).append(" руб.\n");
        return sb.toString();
    }
}
