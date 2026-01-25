package app.state;

import features.books.Book;
import features.customers.Customer;
import features.orders.Order;
import features.requests.Request;

import java.io.Serializable;
import java.util.List;

public class AppState implements Serializable {

    private static final long serialVersionUID = 12345L;

    private final List<Book> bookState;
    private final List<Customer> customerState;
    private final List<Order> orderState;
    private final List<Request> requestState;

    public AppState(List<Book> booksState,
                    List<Customer> customersState,
                    List<Order> ordersState,
                    List<Request> requestsState) {
        this.bookState = booksState;
        this.customerState = customersState;
        this.orderState = ordersState;
        this.requestState = requestsState;
    }

    public List<Book> getBookState() {
        return bookState;
    }

    public List<Customer> getCustomerState() {
        return customerState;
    }

    public List<Order> getOrderState() {
        return orderState;
    }

    public List<Request> getRequestState() {
        return requestState;
    }
}
