package task3_4.features.orders;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.common.status.BookStatus;
import task3_4.common.status.OrderStatus;
import task3_4.cvs.applier.OrderCsvDtoApplier;
import task3_4.cvs.exporter.OrderCsvExporter;
import task3_4.cvs.importer.OrderCsvImporter;
import task3_4.dao.db.TransactionManager;
import task3_4.exceptions.domain.DomainException;
import task3_4.exceptions.domain.InvalidOrderStateException;
import task3_4.exceptions.domain.OrderNotFoundException;
import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.features.customers.Customer;
import task3_4.features.customers.CustomerService;
import task3_4.features.requests.RequestService;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Singleton
public class OrderService {
    @Inject
    private TransactionManager tm;

    @Inject
    private OrderRepository repo;

    @Inject
    private RequestService requestService;

    @Inject
    private CustomerService customerService;

    @Inject
    private BookService bookService;

    public OrderService() {
    }

    public OrderRepository getOrderRepository() {
        return repo;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public List<Order> getAllOrders() {
        return repo.findAllOrders();
    }

    public Order getOrderById(long id) {
        Order order = repo.findOrderById(id);
        if (order == null) throw new OrderNotFoundException(id);
        return order;
    }

    public Order createOrder(List<Book> books, Customer customer) {
        return tm.inTransaction(c -> createOrder(c, books, customer));
    }

    private Order createOrder(Connection c, List<Book> books, Customer customer) {
        if (c == null) throw new DomainException("Невозможно создать заказ: connection=null");

        if (customer == null) {
            throw new DomainException("Невозможно создать заказ: покупатель не указан.");
        }
        if (books == null || books.isEmpty()) {
            throw new DomainException("Невозможно создать заказ: список книг пуст.");
        }
        if (books.stream().anyMatch(b -> b == null)) {
            throw new DomainException("Невозможно создать заказ: обнаружена некорректная книга в списке.");
        }

        Order order = new Order();
        order.setOrderedBooks(new ArrayList<>(books));
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);

        double totalPrice = books.stream()
                .mapToDouble(Book::getPrice)
                .sum();
        order.setTotalPrice(totalPrice);

        repo.addOrder(c, order);

        for (Book book : books) {
            if (book.getStatus() == BookStatus.MISSING) {
                requestService.createOrAppendRequest(c, book, order);
            }
        }
        return order;
    }

    public void updateOrder(Order incoming) {
        tm.inTransactionVoid(c -> updateOrder(c, incoming));
    }

    private void updateOrder(Connection c, Order incoming) {
        if (c == null) throw new DomainException("Невозможно обновить заказ: connection=null");
        if (incoming == null) throw new DomainException("Невозможно обновить заказ: данные не указаны.");

        Order existing = repo.findOrderById(incoming.getId());
        if (existing == null) throw new OrderNotFoundException(incoming.getId());

        existing.setOrderedBooks(new ArrayList<>(incoming.getOrderedBooks()));
        existing.setCustomer(incoming.getCustomer());
        existing.setStatus(incoming.getStatus());
        existing.setCompletionDate(incoming.getCompletionDate());
        existing.setTotalPrice(incoming.getTotalPrice());

        repo.updateOrder(c, existing);
    }

    public void attachCustomer(long orderId, Customer customer) {
        tm.inTransactionVoid(c -> attachCustomer(c, orderId, customer));
    }

    private void attachCustomer(Connection c, long orderId, Customer customer) {
        if (c == null) throw new DomainException("Невозможно привязать клиента: connection=null");

        Order order = repo.findOrderById(orderId);
        if (order == null) throw new OrderNotFoundException(orderId);
        if (customer == null) throw new DomainException("Невозможно привязать к заказу пустого клиента.");

        order.setCustomer(customer);
        repo.updateOrder(c, order);
    }

    public void deleteOrder(long id) {
        tm.inTransactionVoid(c -> {
            boolean removed = repo.deleteOrderById(c, id);
            if (!removed) throw new OrderNotFoundException(id);
        });
    }

    public void completeOrder(long id) {
        tm.inTransactionVoid(c -> completeOrder(c, id));
    }

    private void completeOrder(Connection c, long id) {
        if (c == null) throw new DomainException("Невозможно завершить заказ: connection=null");

        Order order = repo.findOrderById(id);
        if (order == null) throw new OrderNotFoundException(id);

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidOrderStateException(id, "заказ уже завершён.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(id, "невозможно завершить отменённый заказ.");
        }

        List<Book> books = order.getOrderedBooks();
        if (books == null || books.isEmpty()) {
            throw new InvalidOrderStateException(id, "невозможно завершить заказ без книг.");
        }

        boolean hasMissing = books.stream()
                .anyMatch(b -> b.getStatus() == BookStatus.MISSING);

        if (hasMissing) {
            throw new InvalidOrderStateException(
                    id,
                    "невозможно завершить заказ: в заказе есть отсутствующие книги."
            );
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletionDate(LocalDate.now());

        repo.updateOrder(c, order);
    }

    public void cancelOrder(long id) {
        tm.inTransactionVoid(c -> cancelOrder(c, id));
    }

    private void cancelOrder(Connection c, long id) {
        if (c == null) throw new DomainException("Невозможно отменить заказ: connection=null");

        Order order = repo.findOrderById(id);
        if (order == null) throw new OrderNotFoundException(id);

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidOrderStateException(id, "невозможно отменить завершённый заказ.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(id, "заказ уже отменён.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        repo.updateOrder(c, order);
    }

    public void importOrdersFromCsv(String filePath,
                                    OrderCsvImporter importer,
                                    OrderCsvDtoApplier applier) {
        List<OrderCsvDto> dtos = importer.importFrom(filePath);
        for (OrderCsvDto dto : dtos) {
            applier.apply(dto);
        }
    }

    public void exportOrdersToCsv(String filePath,
                                  OrderCsvExporter exporter) {
        exporter.exportTo(filePath, repo.findAllOrders());
    }
}
