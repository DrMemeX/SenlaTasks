package task3_4.view.action.orders.business;

import task3_4.exceptions.domain.DomainException;
import task3_4.features.books.Book;
import task3_4.features.books.BookService;
import task3_4.features.customers.Customer;
import task3_4.features.customers.CustomerService;
import task3_4.features.orders.OrderService;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderAction implements IAction {

    private final OrderService orderService;
    private final BookService bookService;
    private final CustomerService customerService;

    public CreateOrderAction(OrderService orderService,
                             BookService bookService,
                             CustomerService customerService) {
        this.orderService = orderService;
        this.bookService = bookService;
        this.customerService = customerService;
    }

    @Override
    public String title() {
        return "Создать заказ";
    }

    @Override
    public void execute() {

        ConsoleView.title("Создание заказа");

        List<Book> books = new ArrayList<>();

        while (true) {
            long bookId = In.get().intInRange(
                    "Введите ID книги (0 — завершить выбор): ",
                    0,
                    Integer.MAX_VALUE
            );

            if (bookId == 0) break;

            try {
                Book b = bookService.getBookById(bookId);
                books.add(b);
                ConsoleView.ok("Книга добавлена.");
            } catch (DomainException e) {
                ConsoleView.warn(e.getMessage());
            }
        }

        if (books.isEmpty()) {
            ConsoleView.warn("Список книг пуст. Заказ не создан.");
            return;
        }

        try {
            long customerId = In.get().intInRange(
                    "Введите ID покупателя: ",
                    1,
                    Integer.MAX_VALUE
            );

            Customer c = customerService.getCustomerById(customerId);

            orderService.createOrder(books, c);

            ConsoleView.ok("Заказ успешно создан!");

        } catch (DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (Exception e) {
            ConsoleView.warn("Ошибка при создании заказа: " + e.getMessage());
        }
    }
}
