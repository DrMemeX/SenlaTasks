package task3_4.features.orders;

import task3_4.common.status.BookStatus;
import task3_4.common.status.OrderStatus;
import task3_4.exceptions.domain.DomainException;
import task3_4.exceptions.domain.InvalidOrderStateException;
import task3_4.exceptions.domain.OrderNotFoundException;
import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.features.customers.Customer;
import task3_4.features.customers.CustomerService;
import task3_4.features.requests.RequestService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderRepository repo;
    private final RequestService requestService;
    private final CustomerService customerService;
    private final BookService bookService;

    public OrderService(OrderRepository repo,
                        RequestService requestService,
                        CustomerService customerService,
                        BookService bookService) {
        this.repo = repo;
        this.requestService = requestService;
        this.customerService = customerService;
        this.bookService = bookService;
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
        if (order == null) {
            throw new OrderNotFoundException(id);
        }
        return order;
    }


    public Order createOrder(List<Book> books, Customer customer) {

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

        for (Book book : books) {
            if (book.getStatus() == BookStatus.MISSING) {
                requestService.createOrAppendRequest(book, order);
            }
        }

        repo.addOrder(order);
        return order;
    }


    public void deleteOrder(long id) {
        boolean removed = repo.deleteOrderById(id);
        if (!removed) {
            throw new OrderNotFoundException(id);
        }
    }


    public void updateOrder(Order incoming) {
        Order existing = repo.findOrderById(incoming.getId());
        if (existing == null) {
            throw new OrderNotFoundException(incoming.getId());
        }

        existing.setOrderedBooks(new ArrayList<>(incoming.getOrderedBooks()));
        existing.setCustomer(incoming.getCustomer());
        existing.setStatus(incoming.getStatus());
        existing.setCompletionDate(incoming.getCompletionDate());
        existing.setTotalPrice(incoming.getTotalPrice());
    }

    public void attachCustomer(long orderId, Customer customer) {
        Order order = repo.findOrderById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        if (customer == null) {
            throw new DomainException("Невозможно привязать к заказу пустого клиента.");
        }
        order.setCustomer(customer);
    }


    public void completeOrder(long id) {
        Order order = repo.findOrderById(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }

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
    }

    public void cancelOrder(long id) {
        Order order = repo.findOrderById(id);
        if (order == null) {
            throw new OrderNotFoundException(id);
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new InvalidOrderStateException(
                    id,
                    "невозможно отменить завершённый заказ."
            );
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(
                    id,
                    "заказ уже отменён."
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
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
