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
            String s = In.get().line("Введите ID книги (или пустую строку для завершения): ");
            if (s.isBlank()) break;

            try {
                long id = Long.parseLong(s);
                Book b = bookService.getBookById(id);

                books.add(b);
                ConsoleView.ok("Книга добавлена.");

            } catch (NumberFormatException e) {
                ConsoleView.warn("Некорректный формат ID книги.");
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
