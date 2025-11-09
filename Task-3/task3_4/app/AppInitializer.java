package task3_4.app;

import task3_4.model.catalog.Book;
import task3_4.model.customer.Customer;
import task3_4.model.service.BookStore;
import task3_4.model.service.BookStoreReports;
import task3_4.model.status.BookStatus;

import java.time.LocalDate;
import java.util.List;

public class AppInitializer {
    private AppInitializer() {}
    public static void initialize(BookStore store) {
        Book b1 = new Book(
                "Преступление и наказание", "Ф. Достоевский", 500.0, BookStatus.AVAILABLE,
                LocalDate.of(1866, 1, 1), LocalDate.now().minusDays(7),
                "Роман о нравственном падении и искуплении. Классика русской литературы.");

        Book b2 = new Book(
                "Мастер и Маргарита", "М. Булгаков", 600.0, BookStatus.MISSING,
                LocalDate.of(1967, 1, 1), LocalDate.now().minusDays(3),
                "Сатирический роман о Москве 1930-х, любви и мистике.");

        Book b3 = new Book(
                "Отцы и дети", "И. Тургенев", 450.0, BookStatus.AVAILABLE,
                LocalDate.of(1862, 1, 1), LocalDate.now().minusDays(10),
                "Поколенческий конфликт и рождение нигилизма.");

        Book b4 = new Book(
                "Идиот", "Ф. Достоевский", 480.0, BookStatus.AVAILABLE,
                LocalDate.of(1869, 1, 1), LocalDate.now().minusDays(2),
                "Князь Мышкин и мир, который испытывает доброту на прочность.");

        Book b5 = new Book(
                "Обломов", "И. Гончаров", 520.0, BookStatus.MISSING,
                LocalDate.of(1859, 1, 1), LocalDate.now().minusDays(1),
                "Роман о «обломовщине»: апатии, мечте и бездействии.");

        store.addBookToStock(b1);
        store.addBookToStock(b2);
        store.addBookToStock(b3);
        store.addBookToStock(b4);
        store.addBookToStock(b5);

        Customer c1 = new Customer(
                "Андрей Соколов",
                "+7 (495) 123-45-67",
                "a.sokolov@mail.ru",
                "Москва, ул. Тверская, д. 7, кв. 12");

        Customer c2 = new Customer(
                "Екатерина Морозова",
                "+7 (812) 987-65-43",
                "k.morozova@yandex.ru",
                "Санкт-Петербург, Невский пр., д. 28, кв. 34");

        BookStoreReports reports = new BookStoreReports(store, true);

        store.createOrder(List.of(b1, b2, b3), c1);
        store.createOrder(List.of(b4, b5), c2);

        store.completeOrder(1);
        store.cancelOrder(2);
    }
}
