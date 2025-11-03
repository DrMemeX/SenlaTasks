package task3_4.service;

import task3_4.catalog.Book;
import task3_4.catalog.BookRequest;
import task3_4.catalog.Order;
import task3_4.status.BookStatus;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Book> books;
    private List<BookRequest> requests;

    public Inventory() {
        this.books = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void markBookMissing(Book book) {
        book.setStatus(BookStatus.MISSING);
        System.out.println("Книга «" + book.getTitle() + "» отмечена как отсутствующая.");

        BookRequest request = findRequestByBook(book);
        if (request == null) {
            requests.add(new BookRequest(book));
        }
    }

    public void requestBook(Book book, Order order) {
        BookRequest request = findRequestByBook(book);
        if (request == null) {
            request = new BookRequest(book);
            requests.add(request);
        }
        request.addWaitingOrder(order);
    }

    public void fulfillRequest(Book book) {
        BookRequest request = findRequestByBook(book);
        if (request != null && !request.isResolved()) {
            request.resolve();
            book.setStatus(BookStatus.AVAILABLE);
            System.out.println("\nЗапрос на книгу «" + book.getTitle() + "» выполнен, книга поступила в продажу.");
        }
    }

    public List<Book> getBooks() {return books;}
    public List<BookRequest> getRequests() {return requests;}


    private BookRequest findRequestByBook(Book book) {
        for (BookRequest request : requests) {
            if (request.getBook().equals(book)) {
                return request;
            }
        }
        return null;
    }
}
