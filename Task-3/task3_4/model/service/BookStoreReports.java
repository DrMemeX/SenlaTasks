package task3_4.model.service;


import task3_4.model.catalog.Book;
import task3_4.model.catalog.BookRequest;
import task3_4.model.catalog.Order;
import task3_4.model.customer.Customer;
import task3_4.model.status.BookStatus;
import task3_4.model.status.OrderStatus;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookStoreReports {
    private final BookStore store;
    private final boolean ascending;
    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookStoreReports(BookStore store, boolean ascending) {
        this.store = store;
        this.ascending = ascending;
    }

    public void showBooksSortedByTitle(boolean ascending) {
        List<Book> books = store.getInventory().getBooks();
        Comparator<Book> comparator = Comparator.comparing(Book::getTitle);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(books, comparator);
        System.out.println(ascending
                ? "\nСписок книг по алфавиту (А → Я):"
                : "\nСписок книг по алфавиту (Я → А):");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showBooksSortedByReleaseDate() {
        List<Book> books = store.getInventory().getBooks();
        Comparator<Book> comparator = Comparator.comparing(Book::getReleaseDate);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(books, comparator);
        System.out.println(ascending
                ? "\nКниги по дате выпуска (старые → новые):"
                : "\nКниги по дате выпуска (новые → старые):");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showBooksSortedByPrice() {
        List<Book> books = store.getInventory().getBooks();
        Comparator<Book> comparator = Comparator.comparing(Book::getPrice);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(books, comparator);
        System.out.println(ascending
                ? "\nКниги по цене (дешёвые → дорогие):"
                : "\nКниги по цене (дорогие → дешёвые):");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showBooksSortedByAvailability() {
        List<Book> books = store.getInventory().getBooks();
        Comparator<Book> comparator = Comparator.comparing(Book::getStatus);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(books, comparator);
        System.out.println(ascending
                ? "\nКниги по наличию (в наличии → отсутствуют):"
                : "\nКниги по наличию (отсутствуют → в наличии):");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showOrdersSortedByCompletionDate(boolean ascending) {
        List<Order> orders = store.getOrders();
        Comparator<Order> comparator = Comparator.comparing(Order::getCompletionDate,
                Comparator.nullsLast(Comparator.naturalOrder()));
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(orders, comparator);
        System.out.println(ascending
                ? "\nЗаказы по дате исполнения (старые → новые):"
                : "\nЗаказы по дате исполнения (новые → старые):");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void showOrdersSortedByPrice(boolean ascending) {
        List<Order> orders = store.getOrders();
        Comparator<Order> comparator = Comparator.comparing(Order::getTotalPrice);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(orders, comparator);
        System.out.println(ascending
                ? "\nЗаказы по сумме (меньше → больше):"
                : "\nЗаказы по сумме (больше → меньше):");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void showOrdersSortedByStatus(boolean ascending) {
        List<Order> orders = store.getOrders();
        Comparator<Order> comparator = Comparator.comparing(Order::getStatus);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(orders, comparator);
        System.out.println(ascending
                ? "\nЗаказы по статусу (новые → выполненные → отменённые):"
                : "\nЗаказы по статусу (отменённые → выполненные → новые):");
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void showRequestsSortedByCount(boolean ascending) {
        List<BookRequest> requests = store.getInventory().getRequests();
        Comparator<BookRequest> comparator =
                Comparator.comparingInt(r -> r.getWaitingOrders().size());
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(requests, comparator);
        System.out.println(ascending
                ? "\nЗапросы по количеству ожиданий (меньше → больше):"
                : "\nЗапросы по количеству ожиданий (больше → меньше):");
        for (BookRequest request : requests) {
            System.out.println(
                    "Книга: " + request.getBook().getTitle() +
                            " — ожиданий: " + request.getWaitingOrders().size());
        }
    }

    public void showRequestsSortedByTitle(boolean ascending) {
        List<BookRequest> requests = store.getInventory().getRequests();
        Comparator<BookRequest> comparator =
                Comparator.comparing(r -> r.getBook().getTitle());
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(requests, comparator);
        System.out.println(ascending
                ? "\nЗапросы по алфавиту (А → Я):"
                : "\nЗапросы по алфавиту (Я → А):");
        for (BookRequest request : requests) {
            System.out.println(
                    "Книга: " + request.getBook().getTitle() +
                            " — ожиданий: " + request.getWaitingOrders().size());
        }
    }

    public void showCompletedOrdersByDate(LocalDate start, LocalDate end, boolean ascending) {
        List<Order> orders = store.getOrders();
        List<Order> completed = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.COMPLETED
                    && o.getCompletionDate() != null
                    && !o.getCompletionDate().isBefore(start)
                    && !o.getCompletionDate().isAfter(end)) {
                completed.add(o);
            }
        }
        Comparator<Order> comparator = Comparator.comparing(Order::getCompletionDate);
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(completed, comparator);
        System.out.println(ascending
                ? "\nВыполненные заказы по дате (старые → новые):"
                : "\nВыполненные заказы по дате (новые → старые):");
        for (Order o : completed) {
            System.out.println(o);
        }
    }

    public void showCompletedOrdersByPrice(LocalDate start, LocalDate end, boolean ascending) {
        List<Order> completed = new ArrayList<>();
        for (Order o : store.getOrders()) {
            if (o.getStatus() == OrderStatus.COMPLETED
                    && o.getCompletionDate() != null
                    && !o.getCompletionDate().isBefore(start)
                    && !o.getCompletionDate().isAfter(end)) {
                completed.add(o);
            }
        }
        Comparator<Order> byPrice = Comparator.comparing(Order::getTotalPrice);
        if (!ascending) byPrice = byPrice.reversed();
        completed.sort(byPrice);

        System.out.println(ascending
                ? "\nВыполненные заказы по цене (меньше → больше):"
                : "\nВыполненные заказы по цене (больше → меньше):");
        for (Order o : completed) System.out.println(o);
        if (completed.isEmpty()) System.out.println("Нет выполненных заказов в выбранном периоде.");
    }

    public void showTotalIncome(LocalDate start, LocalDate end) {
        List<Order> orders = store.getOrders();
        double total = 0.0;
        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.COMPLETED
                    && o.getCompletionDate() != null
                    && !o.getCompletionDate().isBefore(start)
                    && !o.getCompletionDate().isAfter(end)) {
                total += o.getTotalPrice();
            }
        }
        System.out.println("\nПериод: " + start + " — " + end);
        System.out.println("Сумма заработанных средств за период: " + total + " руб.");
    }

    public void showCompletedOrdersCount(LocalDate start, LocalDate end) {
        List<Order> orders = store.getOrders();
        int count = 0;
        for (Order o : orders) {
            if (o.getStatus() == OrderStatus.COMPLETED
                    && o.getCompletionDate() != null
                    && !o.getCompletionDate().isBefore(start)
                    && !o.getCompletionDate().isAfter(end)) {
                count++;
            }
        }
        System.out.println("\nПериод: " + start + " — " + end);
        System.out.println("Количество выполненных заказов за период: " + count);
    }

    public void showOldBooks(boolean sortByDate, boolean sortByPrice, boolean ascending) {
        List<Book> books = store.getInventory().getBooks();
        List<Book> oldBooks = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for (Book book : books) {
            if (book.getAddedDate() != null &&
                    ChronoUnit.MONTHS.between(book.getAddedDate(), now) > 6 &&
                    book.getStatus() == BookStatus.AVAILABLE) {
                oldBooks.add(book);
            }
        }
        Comparator<Book> comparator;

        if (sortByDate) {
            System.out.println("Сортировка по дате поступления:");
            comparator = Comparator.comparing(Book::getAddedDate);
        } else if (sortByPrice) {
            System.out.println("Сортировка по цене:");
            comparator = Comparator.comparing(Book::getPrice);
        } else {
            System.out.println("Не указано поле сортировки. Используется сортировка дате поступления:");
            comparator = Comparator.comparing(Book::getAddedDate);
        }
        if (!ascending) comparator = comparator.reversed();
        Collections.sort(oldBooks, comparator);
        System.out.println(ascending
                ? "\nЗалежавшиеся книги (по возрастанию):"
                : "\nЗалежавшиеся книги (по убыванию):");
        for (Book b : oldBooks) {
            System.out.println("Название: " + b.getTitle() +
                    " | Дата поступления: " + b.getAddedDate() +
                    " | Цена: " + b.getPrice() + " руб.");
        }
        if (oldBooks.isEmpty()) {
            System.out.println("Нет книг, залежавшихся более 6 месяцев.");
        }
    }

    public void showOrderDetails(int orderId) {
        Order order = store.getOrders().stream()
                .filter(o -> o.getId() == orderId)
                .findFirst().orElse(null);
        showOrderDetails(order);
    }

    public void showOrderDetails(Order order) {
        if (order == null) {
            System.out.println("Заказ не найден.\n");
            return;
        }
        System.out.println("Детали заказа №" + order.getId());
        System.out.println("Статус: " + order.getStatus());
        if (order.getCompletionDate() != null) {
            System.out.println("Завершён: " + order.getCompletionDate().format(DF));
        } else {
            System.out.println("Завершён: -");
        }
        System.out.printf("Сумма: %.2f%n", order.getTotalPrice());
        System.out.println("\nЗаказчик:");
        Customer c = order.getCustomer();
        if (c == null) {
            System.out.println("  — данные не указаны");
        } else {
            System.out.println("  Имя:     " + c.getName());
            System.out.println("  Телефон: " + c.getPhone());
            System.out.println("  Email:   " + c.getEmail());
            System.out.println("  Адрес:   " + c.getAddress());
        }
        System.out.println("\nКниги:");
        System.out.printf("  %-35s %-10s %-12s%n", "Название", "Цена", "Статус");
        for (Book b : order.getBooks()) {
            System.out.printf("  %-35s %-10.2f %-12s%n",
                    b.getTitle(), b.getPrice(), b.getStatus());
        }
        if (order.hasMissingBooks()) {
            System.out.println("\nВ заказе есть отсутствующие книги — оформлены запросы на поставку.");
        }
        System.out.println();
    }

    public String getBookDescription(Book book) {
        if (book == null) return null;
        String d = book.getDescription();
        return (d == null || d.isBlank()) ? "— описание не указано —" : d.trim();
    }

    public String getBookDescription(String title) {
        Book b = store.findBookByTitle(title);
        return getBookDescription(b);
    }

    public String getBookDescription(String title, String author) {
        Book b = store.findBookByTitleAndAuthor(title, author);
        return getBookDescription(b);
    }

    public void showBookDescriptionText(Book book) {
        if (book == null) {
            System.out.println("Книга не найдена.\n");
            return;
        }
        String d = book.getDescription();
        System.out.println((d == null || d.isBlank()) ? "— описание не указано —" : d.trim());
        System.out.println("Название: «" + book.getTitle() + "»");
    }

    public void showBookDescriptionText(String title) {
        Book b = store.findBookByTitle(title);
        showBookDescriptionText(b);
    }

    public void showBookDescriptionText(String title, String author) {
        Book b = store.findBookByTitleAndAuthor(title, author);
        showBookDescriptionText(b);
    }
}
