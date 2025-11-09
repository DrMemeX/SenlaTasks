package task3_4.model.catalog;

import java.util.ArrayList;
import java.util.List;

public class BookRequest {
    private Book book;
    private List<Order> waitingOrders;
    private boolean resolved;

    public BookRequest(Book book) {
        this.book = book;
        this.waitingOrders = new ArrayList<>();
        this.resolved = false;
    }

    public Book getBook() {return book;}
    public List<Order> getWaitingOrders() {return waitingOrders;}

    public void addWaitingOrder(Order order) {waitingOrders.add(order);}

    public void resolve() {
        this.resolved = true;
    }

    public boolean isResolved() {return resolved;}


    @Override
    public String toString() {
        return String.format("Запрос на книгу: «%s» (автор: %s)\nСтатус: %s\nКоличество заказов, ожидающих поступления книги: %d\n",
                book.getTitle(),
                book.getAuthor(),
                resolved ? "Выполнен" : "Ожидает поступления",
                waitingOrders.size());
    }
}
