package task3_4.features.requests;

import task3_4.features.books.Book;
import task3_4.features.orders.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Request implements Serializable {
    private static final long serialVersionUID = 1234L;

//    private static long nextId = 1;

    private long id;

    private long bookId;
    private Book book;

    private List<Order> waitingOrders;
    private boolean resolved;

    public Request(Book book) {
//        this.id = nextId++;
        this.book = book;
        if (book != null) {
            this.bookId = book.getId();
        }
        this.waitingOrders = new ArrayList<>();
        this.resolved = false;
    }

    public Request(long id, Book book) {
        this.id = id;
        this.book = book;
        if (book != null) {
            this.bookId = book.getId();
        }
        this.waitingOrders = new ArrayList<>();
        this.resolved = false;

//        if (id >= nextId) {
//            nextId = id + 1;
//        }
    }

//    public static long getNextId() {return nextId;}

    public long getId() {return id;}
    public boolean isNew() {
        return id == 0;
    }

    public long getBookId() {
        if (book != null) return book.getId();
        return bookId;
    }
    public Book getBook() {return book;}

    public List<Order> getWaitingOrders() {return waitingOrders;}
    public boolean isResolved() {return resolved;}

//    public static void setNextId(long nextId) {Request.nextId = nextId;}

    public void setId(long id) {
        this.id = id;
//        if (id >= nextId) {
//            nextId = id + 1;
//        }
    }
    public void setBookId(long bookId) { this.bookId = bookId; }
    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            this.bookId = book.getId();
        }
    }
    public void addWaitingOrder(Order order) {waitingOrders.add(order);}
    public void setResolved(boolean resolved) {this.resolved = resolved;}

    public void resolve() {this.resolved = true;}

    @Override
    public String toString() {
        return String.format(
                "Запрос на книгу: «%s» (автор: %s)\nСтатус: %s\nКоличество заказов, ожидающих поступления книги: %d\n",
                book != null ? book.getTitle() : ("bookId=" + bookId),
                book != null ? book.getAuthor() : "—",
                resolved ? "Выполнен" : "Ожидает поступления",
                waitingOrders.size()
        );
    }
}
