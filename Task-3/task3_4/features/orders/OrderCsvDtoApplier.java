package task3_4.features.orders;

import task3_4.exceptions.csv.CsvMappingException;
import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.features.customers.Customer;
import task3_4.features.customers.CustomerService;

import java.util.ArrayList;
import java.util.List;

public class OrderCsvDtoApplier {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final BookService bookService;

    public OrderCsvDtoApplier(OrderRepository orderRepository,
                              CustomerService customerService,
                              BookService bookService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.bookService = bookService;
    }

    public Order apply(OrderCsvDto dto) {

        Customer customer = customerService.getCustomerById(dto.customerId);
        if (customer == null) {
            throw new CsvMappingException(
                    "Ошибка маппинга заказа ID=" + dto.id +
                            ": клиент ID=" + dto.customerId + " не найден."
            );
        }

        Order order = orderRepository.findOrderById(dto.id);
        if (order == null) {
            order = new Order(dto.id);
            orderRepository.addOrder(order);
        }

        order.setCustomer(customer);

        List<Book> orderedBooks = new ArrayList<>();

        for (Long bookId : dto.bookIds) {
            Book book = bookService.getBookById(bookId);

            if (book == null) {
                throw new CsvMappingException(
                        "Ошибка маппинга заказа ID=" + dto.id +
                                ": книга ID=" + bookId + " отсутствует в базе."
                );
            }

            orderedBooks.add(book);
        }

        order.setOrderedBooks(orderedBooks);

        order.setStatus(dto.status);

        if (dto.creationDate == null) {
            throw new CsvMappingException(
                    "Ошибка маппинга заказа ID=" + dto.id +
                            ": отсутствует дата создания заказа."
            );
        }
        order.setCreationDate(dto.creationDate);

        if (dto.completionDate != null &&
                dto.completionDate.isBefore(dto.creationDate)) {

            throw new CsvMappingException(
                    "Ошибка маппинга заказа ID=" + dto.id +
                            ": дата завершения заказа не может быть раньше даты создания заказа."
            );
        }

        order.setCompletionDate(dto.completionDate);

        double booksSum = orderedBooks.stream()
                .mapToDouble(Book::getPrice)
                .sum();

        if (Math.abs(booksSum - dto.totalPrice) > 0.01) {
            throw new CsvMappingException(
                    "Ошибка маппинга заказа ID=" + dto.id +
                            ": общая стоимость заказа = " + dto.totalPrice +
                            " не совпадает с суммой книг=" + booksSum
            );
        }

        order.setTotalPrice(dto.totalPrice);

        return order;
    }
}
