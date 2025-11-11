package task3_4.app;

import task3_4.model.catalog.Book;
import task3_4.model.customer.Customer;
import task3_4.model.service.BookStore;
import task3_4.model.service.BookStoreReports;
import task3_4.model.status.BookStatus;

import java.time.LocalDate;
import java.util.List;

public final class Demo {
    private Demo() {}

    public static void run() {
        System.out.println("\n=== ДЕМО РЕЖИМ ===\n");

        BookStore store = new BookStore();
        BookStoreReports reports = new BookStoreReports(store, true);

        Book b1 = new Book(
                "Преступление и наказание", "Ф. Достоевский", 500.0, BookStatus.AVAILABLE,
                LocalDate.of(1866, 1, 1),     // releaseDate
                LocalDate.now().minusDays(7), // addedDate
                "Роман о нравственном падении и искуплении. Классика русской литературы.");
        Book b2 = new Book(
                "Мастер и Маргарита", "М. Булгаков", 600.0, BookStatus.MISSING,
                LocalDate.of(1967, 1, 1),
                LocalDate.now().minusDays(3),
                "Сатирический роман о Москве 1930-х, любви и мистике.");
        Book b3 = new Book(
                "Отцы и дети", "И. Тургенев", 450.0, BookStatus.AVAILABLE,
                LocalDate.of(1862, 1, 1),
                LocalDate.now().minusDays(10),
                "Поколенческий конфликт и рождение нигилизма.");
        Book b4 = new Book(
                "Идиот", "Ф. Достоевский", 480.0, BookStatus.AVAILABLE,
                LocalDate.of(1869, 1, 1),
                LocalDate.now().minusDays(2),
                "Князь Мышкин и мир, который испытывает доброту на прочность.");
        Book b5 = new Book(
                "Обломов", "И. Гончаров", 520.0, BookStatus.MISSING,
                LocalDate.of(1859, 1, 1),
                LocalDate.now().minusDays(1),
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

        System.out.println("\nОФОРМЛЕНИЕ ЗАКАЗА №1");
        store.createOrder(List.of(b1, b2, b3), c1);

        System.out.println("\nОФОРМЛЕНИЕ ЗАКАЗА №2");
        store.createOrder(List.of(b4, b5), c2);

        store.showAllOrders();

        System.out.println("\nПОПЫТКА ЗАВЕРШИТЬ ЗАКАЗ №1");
        store.completeOrder(1);

        store.addBookToStock(b2);

        System.out.println("\nПОВТОРНАЯ ПОПЫТКА ЗАВЕРШИТЬ ЗАКАЗ №1");
        store.completeOrder(1);

        System.out.println("\nДЕТАЛИ ЗАКАЗА №1");
        reports.showOrderDetails(1);

        System.out.println("\nОТМЕНА ЗАКАЗА №2");
        store.cancelOrder(2);

        System.out.println("\nИТОГОВОЕ СОСТОЯНИЕ");
        store.showAllOrders();
        store.showActiveRequests();
        store.showBooksInStock();

        System.out.println("\n=== СПИСКИ КНИГ (сортировки) ===");
        reports.showBooksSortedByTitle(true);
        reports.showBooksSortedByTitle(false);
        reports.showBooksSortedByReleaseDate();
        reports.showBooksSortedByPrice();
        reports.showBooksSortedByAvailability();

        System.out.println("\n=== СПИСОК ЗАКАЗОВ (сортировки) ===");
        reports.showOrdersSortedByCompletionDate(true);
        reports.showOrdersSortedByCompletionDate(false);
        reports.showOrdersSortedByPrice(true);
        reports.showOrdersSortedByPrice(false);
        reports.showOrdersSortedByStatus(true);
        reports.showOrdersSortedByStatus(false);

        System.out.println("\n=== СПИСОК ЗАПРОСОВ НА КНИГИ (сортировки) ===");
        reports.showRequestsSortedByCount(true);
        reports.showRequestsSortedByCount(false);
        reports.showRequestsSortedByTitle(true);
        reports.showRequestsSortedByTitle(false);

        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end   = LocalDate.now().plusDays(1);

        System.out.println("\n=== ВЫПОЛНЕННЫЕ ЗАКАЗЫ ЗА ПЕРИОД ===");
        reports.showCompletedOrdersByDate(start, end, true);
        reports.showCompletedOrdersByPrice(start, end, true);

        System.out.println("\n=== КОЛИЧЕСТВО ВЫПОЛНЕННЫХ ЗАКАЗОВ ЗА ПЕРИОД ===");
        reports.showCompletedOrdersCount(start, end);

        System.out.println("\n=== «ЗАЛЕЖАВШИЕСЯ» КНИГИ (>6 мес, сортировки) ===");
        reports.showOldBooks(true,  false, true);
        reports.showOldBooks(false, true,  true);

        System.out.println("\n=== ДЕТАЛИ КОНКРЕТНЫХ ЗАКАЗОВ ===");
        reports.showOrderDetails(1);
        reports.showOrderDetails(2);

        System.out.println("\nПРОСМОТР ОПИСАНИЯ (по объекту)");
        reports.showBookDescriptionText(b4);

        System.out.println("\nПРОСМОТР ОПИСАНИЯ (по названию и автору)");
        reports.showBookDescriptionText("Преступление и наказание", "Ф. Достоевский");

        System.out.println("\nПРОСМОТР ОПИСАНИЯ (по одному названию)");
        reports.showBookDescriptionText("Идиот");
    }
}
