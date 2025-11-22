package task3_4.features.requests;

import task3_4.features.books.Book;
import task3_4.features.orders.Order;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private static long nextId = 1;

    private long id;
    private Book book;
    private List<Order> waitingOrders;
    private boolean resolved;

    public Request(Book book) {
        this.id = nextId++;
        this.book = book;
        this.waitingOrders = new ArrayList<>();
        this.resolved = false;
    }

    public Request(long id, Book book) {
        this.id = id;
        this.book = book;
        this.waitingOrders = new ArrayList<>();
        this.resolved = false;

        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public static long getNextId() {return nextId;}

    public long getId() {return id;}
    public Book getBook() {return book;}
    public List<Order> getWaitingOrders() {return waitingOrders;}
    public boolean isResolved() {return resolved;}

    public static void setNextId(long nextId) {Request.nextId = nextId;}

    public void setId(long id) {
        this.id = id;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    public void setBook(Book book) {this.book = book;}
    public void addWaitingOrder(Order order) {waitingOrders.add(order);}
    public void setResolved(boolean resolved) {this.resolved = resolved;}

    public void resolve() {this.resolved = true;}

    @Override
    public String toString() {
        return String.format("Запрос на книгу: «%s» (автор: %s)\nСтатус: %s\nКоличество заказов, ожидающих поступления книги: %d\n",
                book.getTitle(),
                book.getAuthor(),
                resolved ? "Выполнен" : "Ожидает поступления",
                waitingOrders.size());
    }
}
