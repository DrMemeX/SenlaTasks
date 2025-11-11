package task3_4.model.catalog;

import task3_4.model.customer.Customer;
import task3_4.model.status.BookStatus;
import task3_4.model.status.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private int id;
    private List<Book> books;
    private OrderStatus status;
    private LocalDate completionDate;
    private double totalPrice;
    private Customer customer;

    public Order(int id) {
        this.id = id;
        this.books = new ArrayList<>();
        this.status = OrderStatus.NEW;
        this.completionDate = LocalDate.now();
        this.totalPrice = 0.0;
    }

    public int getId() {return id;}

    public List<Book> getBooks() {return books;}
    public OrderStatus getStatus() {return status;}
    public LocalDate getCompletionDate() {return completionDate;}
    public double getTotalPrice() {return totalPrice;}
    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public void addBook(Book book) {
        books.add(book);
        totalPrice += book.getPrice();
    }

    public boolean hasMissingBooks() {
        for (Book book : books) {
            if (BookStatus.MISSING == book.getStatus()) {
                return true;
            }
        }
        return false;
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Заказ №").append(id).append("\n");
        sb.append("Дата: ").append(completionDate).append("\n");
        sb.append("Статус: ").append(status).append("\n");
        sb.append("Состав заказа:\n");
        for (Book book : books) {
            sb.append(" — ").append(book.getTitle())
              .append(" — ").append(book.getAuthor())
              .append(" (").append(book.getPrice()).append(" руб.)")
              .append(" [").append(book.getStatus()).append("]\n");
        }
        sb.append("Итого: ").append(totalPrice).append(" руб.\n");
        return sb.toString();
    }
}
