package task3_4.view.factory;

import task3_4.model.catalog.Book;
import task3_4.model.catalog.Order;
import task3_4.model.service.BookStore;
import task3_4.model.service.BookStoreReports;
import task3_4.view.action.IAction;
import task3_4.view.menu.Menu;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.time.LocalDate;

public final class DefaultUiActionFactory implements IUiActionFactory {
    private final BookStore store;
    private final BookStoreReports reports;

    public  DefaultUiActionFactory(BookStore store) {
        this.store = store;
        this.reports = new BookStoreReports(store, true);
    }

    private IAction action(String title, Runnable r) {
        return new IAction() {
            @Override
            public String title() {return title;}

            @Override
            public void execute() {r.run();}
        };
    }

    private Menu booksMenu() {
        Menu m = new Menu("Книги");

        m.add("Показать все книги в наличии",
                action("list_books", store::showBooksInStock));

        m.add("Сортировать по названию (A→Z)",
                action("sort_books_title_asc", () -> reports.showBooksSortedByTitle(true)));

        m.add("Сортировать по названию (Z→A)",
                action("sort_books_title_desc", () -> reports.showBooksSortedByTitle(false)));

        m.add("Сортировать по дате выпуска",
                action("sort_books_date", reports::showBooksSortedByReleaseDate));

        m.add("Сортировать по цене",
                action("sort_books_price", reports::showBooksSortedByPrice));

        m.add("Сортировать по наличию",
                action("sort_books_availability", reports::showBooksSortedByAvailability));

        m.add("Описание книги",
                action("book_description", () -> {
                    String title = In.get().line("Введите название книги: ");
                    Book b = store.findBookByTitle(title);
                    if (b == null) ConsoleView.warn("Книга не найдена.");
                    else reports.showBookDescriptionText(b);
                }));

        return m;
    }

    private Menu ordersMenu() {
        Menu m = new Menu("Заказы");

        m.add("Показать все заказы",
                action("show_orders", store::showAllOrders));

        m.add("Сортировать по дате исполнения (↑)",
                action("sort_orders_date_asc", () -> reports.showOrdersSortedByCompletionDate(true)));

        m.add("Сортировать по дате исполнения (↓)",
                action("sort_orders_date_desc", () -> reports.showOrdersSortedByCompletionDate(false)));

        m.add("Сортировать по цене (↑)",
                action("sort_orders_price_asc", () -> reports.showOrdersSortedByPrice(true)));

        m.add("Сортировать по цене (↓)",
                action("sort_orders_price_desc", () -> reports.showOrdersSortedByPrice(false)));

        m.add("Сортировать по статусу (A→Z)",
                action("sort_orders_status_asc", () -> reports.showOrdersSortedByStatus(true)));

        m.add("Сортировать по статусу (Z→A)",
                action("sort_orders_status_desc", () -> reports.showOrdersSortedByStatus(false)));

        m.add("Посмотреть детали заказа",
                action("order_details", () -> {
                    int id = In.get().intInRange("Введите номер заказа: ", 1, Integer.MAX_VALUE);
                    reports.showOrderDetails(id);
                }));

        return m;
    }

    private Menu requestsMenu() {
        Menu m = new Menu("📦 Запросы на книги");

        m.add("Показать активные запросы",
                action("show_requests", store::showActiveRequests));

        m.add("Сортировать по алфавиту (A→Z)",
                action("requests_by_title_asc", () -> reports.showRequestsSortedByTitle(true)));

        m.add("Сортировать по алфавиту (Z→A)",
                action("requests_by_title_desc", () -> reports.showRequestsSortedByTitle(false)));

        m.add("Сортировать по количеству запросов (↑)",
                action("requests_by_count_asc", () -> reports.showRequestsSortedByCount(true)));

        m.add("Сортировать по количеству запросов (↓)",
                action("requests_by_count_desc", () -> reports.showRequestsSortedByCount(false)));

        return m;
    }

    private Menu reportsMenu() {
        Menu m = new Menu("Отчёты");

        m.add("Выполненные заказы по дате (за период)",
                action("done_orders_period", () -> {
                    LocalDate from = LocalDate.parse(In.get().line("Введите дату начала (ГГГГ-ММ-ДД): "));
                    LocalDate to   = LocalDate.parse(In.get().line("Введите дату окончания (ГГГГ-ММ-ДД): "));
                    reports.showCompletedOrdersByDate(from, to, true);
                }));

        m.add("Выполненные заказы по цене (за период)",
                action("done_orders_by_price", () -> {
                    LocalDate from = LocalDate.parse(In.get().line("Введите дату начала (ГГГГ-ММ-ДД): "));
                    LocalDate to   = LocalDate.parse(In.get().line("Введите дату окончания (ГГГГ-ММ-ДД): "));
                    reports.showCompletedOrdersByPrice(from, to, true);
                }));

        m.add("Сумма заработанных средств за период",
                action("income_period", () -> {
                    LocalDate from = LocalDate.parse(In.get().line("Введите дату начала (ГГГГ-ММ-ДД): "));
                    LocalDate to   = LocalDate.parse(In.get().line("Введите дату окончания (ГГГГ-ММ-ДД): "));
                    reports.showTotalIncome(from, to);
                }));

        m.add("Количество выполненных заказов за период",
                action("count_orders_period", () -> {
                    LocalDate from = LocalDate.parse(In.get().line("Введите дату начала (ГГГГ-ММ-ДД): "));
                    LocalDate to   = LocalDate.parse(In.get().line("Введите дату окончания (ГГГГ-ММ-ДД): "));
                    reports.showCompletedOrdersCount(from, to);
                }));

        m.add("«Залежавшиеся» книги (> 6 мес.)",
                action("stale_books", () -> reports.showOldBooks(true, false, true)));

        return m;
    }

    @Override
    public Menu buildRootMenu() {
        Menu root = new Menu("=== КОНСОЛЬНЫЙ КНИЖНЫЙ МАГАЗИН ===");

        root.add("Книги", booksMenu());
        root.add("Заказы", ordersMenu());
        root.add("Запросы на книги", requestsMenu());
        root.add("Отчёты", reportsMenu());

        return root;
    }
}
