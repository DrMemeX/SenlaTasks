package task3_4.catalog;

import task3_4.status.BookStatus;
import task3_4.status.OrderStatus;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private int id;
    private List<Book> books;
    private String status;

    public Order(int id) {
        this.id = id;
        this.books = new ArrayList<>();
        this.status = OrderStatus.NEW;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean hasMissingBooks() {
        for (Book book : books) {
            if (BookStatus.MISSING.equals(book.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public void changeStatus(String newStatus) {
        this.status = newStatus;
    }

    public int getId() {return id;}
    public String getStatus() {return status;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Заказ №").append(id).append("\n");
        sb.append("Статус: ").append(status).append("\n");
        sb.append("Состав заказа:\n");
        for (Book book : books) {
            sb.append(" — ").append(book.getTitle())
              .append(" — ").append(book.getAuthor())
              .append(" (").append(book.getPrice()).append(" руб.)")
              .append(" [").append(book.getStatus()).append("]\n");
        }
        return sb.toString();
    }
}
