package app;

import common.status.BookStatus;
import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import exceptions.app.AppInitializationException;
import features.books.Book;
import features.books.BookService;
import features.customers.Customer;
import features.customers.CustomerService;
import features.orders.OrderService;
import features.requests.RequestService;
import view.util.ConsoleView;

import java.time.LocalDate;
import java.util.List;

@Component
@Singleton
public class AppInitializer {

    @Inject
    private BookService bookService;

    @Inject
    private CustomerService customerService;

    @Inject
    private OrderService orderService;

    @Inject
    private RequestService requestService;

    public AppInitializer() {
    }

    public void initialize() {

        try {
            ConsoleView.title("Инициализация тестовых данных");

            Book b1 = new Book(
                    "Преступление и наказание", "Ф. Достоевский", 500.0, BookStatus.AVAILABLE,
                    LocalDate.of(1866, 1, 1), LocalDate.now().minusDays(7),
                    "Роман о нравственном падении и искуплении."
            );

            Book b2 = new Book(
                    "Мастер и Маргарита", "М. Булгаков", 600.0, BookStatus.MISSING,
                    LocalDate.of(1967, 1, 1), LocalDate.now().minusDays(3),
                    "Сатирический роман о Москве 1930-х."
            );

            Book b3 = new Book(
                    "Отцы и дети", "И. Тургенев", 450.0, BookStatus.AVAILABLE,
                    LocalDate.of(1862, 1, 1), LocalDate.now().minusDays(10),
                    "Поколенческий конфликт и нигилизм."
            );

            Book b4 = new Book(
                    "Идиот", "Ф. Достоевский", 480.0, BookStatus.AVAILABLE,
                    LocalDate.of(1869, 1, 1), LocalDate.now().minusDays(2),
                    "Князь Мышкин и мир."
            );

            Book b5 = new Book(
                    "Обломов", "И. Гончаров", 520.0, BookStatus.MISSING,
                    LocalDate.of(1859, 1, 1), LocalDate.now().minusDays(1),
                    "Роман о «обломовщине»."
            );

            bookService.addBook(b1);
            bookService.addBook(b2);
            bookService.addBook(b3);
            bookService.addBook(b4);
            bookService.addBook(b5);

            Customer c1 = new Customer(
                    "Андрей Соколов",
                    "+7 (495) 123-45-67",
                    "a.sokolov@mail.ru",
                    "Москва"
            );

            Customer c2 = new Customer(
                    "Екатерина Морозова",
                    "+7 (812) 987-65-43",
                    "k.morozova@yandex.ru",
                    "СПб"
            );

            customerService.addCustomer(c1);
            customerService.addCustomer(c2);

            orderService.createOrder(List.of(b1, b3, b4), c1);

            orderService.createOrder(List.of(b2, b5), c2);

            orderService.completeOrder(1);
            orderService.cancelOrder(2);

            ConsoleView.ok("Тестовые данные успешно загружены.");
        } catch (Exception e) {

            ConsoleView.warn("Не удалось загрузить тестовые данные.");
            ConsoleView.warn("Причина: " + e.getMessage());

            throw new AppInitializationException("Ошибка инициализации приложения.", e);
        }
    }
}
