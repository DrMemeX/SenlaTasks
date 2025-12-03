package task3_4.view.factory;

import task3_4.features.books.BookService;
import task3_4.features.customers.CustomerService;
import task3_4.features.orders.OrderService;
import task3_4.features.requests.RequestService;
import task3_4.features.reports.BookReportService;
import task3_4.features.reports.OrderReportService;
import task3_4.features.reports.RequestReportService;

import task3_4.view.action.customers.buisness.*;
import task3_4.view.menu.Menu;

import task3_4.view.action.books.business.*;
import task3_4.view.action.books.reports.*;

import task3_4.view.action.orders.business.*;
import task3_4.view.action.orders.reports.*;

import task3_4.view.action.requests.business.*;
import task3_4.view.action.requests.reports.*;

public class DefaultUiActionFactory implements IUiActionFactory {

    private final BookService bookService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final RequestService requestService;
    private final BookReportService bookReportService;
    private final OrderReportService orderReportService;
    private final RequestReportService requestReportService;

    public DefaultUiActionFactory(BookService bookService,
                                  CustomerService customerService,
                                  OrderService orderService,
                                  RequestService requestService,
                                  BookReportService bookReportService,
                                  OrderReportService orderReportService,
                                  RequestReportService requestReportService) {
        this.bookService = bookService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.requestService = requestService;
        this.bookReportService = bookReportService;
        this.orderReportService = orderReportService;
        this.requestReportService = requestReportService;
    }

    @Override
    public Menu buildRootMenu() {

        // КНИГИ: бизнес
        Menu booksBusiness = new Menu("Операции с книгами");
        booksBusiness.add("Добавить книгу", new AddBookAction(bookService));
        booksBusiness.add("Удалить книгу", new DeleteBookAction(bookService));
        booksBusiness.add("Импорт книг (CSV)", new ImportBooksAction(bookService));
        booksBusiness.add("Экспорт книг (CSV)", new ExportBooksAction(bookService));
        booksBusiness.add("Обновить данные книги", new UpdateBookAction(bookService));

        // КНИГИ: отчёты
        Menu booksReports = new Menu("Отчёты по книгам");
        booksReports.add("Описание по названию", new BookDescriptionByTitleAction(bookReportService));
        booksReports.add("Описание по названию и автору",
                new BookDescriptionByTitleAndAuthorAction(bookReportService));
        booksReports.add("Сортировать по названию",
                new BooksSortByTitleAction(bookReportService));
        booksReports.add("Сортировать по доступности",
                new BooksSortByAvailabilityAction(bookReportService));
        booksReports.add("Сортировать по цене",
                new BooksSortByPriceAction(bookReportService));
        booksReports.add("Сортировать по дате выпуска",
                new BooksSortByReleaseDateAction(bookReportService));
        booksReports.add("«Старые» книги по дате",
                new OldBooksSortByDateAction(bookReportService));
        booksReports.add("«Старые» книги по цене",
                new OldBooksSortByPriceAction(bookReportService));

        Menu booksMenu = new Menu("Книги");
        booksMenu.add("Бизнес-операции", booksBusiness);
        booksMenu.add("Отчёты по книгам", booksReports);

        // ПОКУПАТЕЛИ
        Menu customersMenu = new Menu("Покупатели");
        customersMenu.add("Добавить покупателя", new AddCustomerAction(customerService));
        customersMenu.add("Удалить покупателя", new DeleteCustomerAction(customerService));
        customersMenu.add("Импорт покупателей (CSV)", new ImportCustomersAction(customerService));
        customersMenu.add("Экспорт покупателей (CSV)", new ExportCustomersAction(customerService));
        customersMenu.add("Обновить данные покупателя", new UpdateCustomerAction(customerService));

        // ЗАКАЗЫ: бизнес
        Menu ordersBusiness = new Menu("Операции с заказами");
        ordersBusiness.add("Создать заказ",
                new CreateOrderAction(orderService, bookService, customerService));
        ordersBusiness.add("Отменить заказ", new CancelOrderAction(orderService));
        ordersBusiness.add("Удалить заказ", new DeleteOrderAction(orderService));
        ordersBusiness.add("Обновить статус заказа",
                new UpdateOrderStatusAction(orderService));
        ordersBusiness.add("Завершить заказ", new CompleteOrderAction(orderService));
        ordersBusiness.add("Импорт заказов (CSV)", new ImportOrdersAction(orderService));
        ordersBusiness.add("Экспорт заказов (CSV)", new ExportOrdersAction(orderService));

        // ЗАКАЗЫ: отчёты
        Menu ordersReports = new Menu("Отчёты по заказам");
        ordersReports.add("Выполненные по дате",
                new CompletedOrdersByDateAction(orderReportService));
        ordersReports.add("Выполненные по цене",
                new CompletedOrdersByPriceAction(orderReportService));
        ordersReports.add("Количество выполненных заказов",
                new CompletedOrdersCountAction(orderReportService));
        ordersReports.add("Сортировать по дате",
                new OrdersSortByDateAction(orderReportService));
        ordersReports.add("Сортировать по цене",
                new OrdersSortByPriceAction(orderReportService));
        ordersReports.add("Сортировать по статусу",
                new OrdersSortByStatusAction(orderReportService));
        ordersReports.add("Детали заказа", new OrderDetailsAction(orderReportService));
        ordersReports.add("Общий доход", new TotalIncomeAction(orderReportService));

        Menu ordersMenu = new Menu("Заказы");
        ordersMenu.add("Бизнес-операции", ordersBusiness);
        ordersMenu.add("Отчёты по заказам", ordersReports);

        // ЗАПРОСЫ: бизнес
        Menu requestsBusiness = new Menu("Операции с запросами");
        requestsBusiness.add("Отметить запрос выполненным",
                new CompleteRequestAction(requestService));
        requestsBusiness.add("Удалить запрос",
                new DeleteRequestAction(requestService));
        requestsBusiness.add("Импорт запросов (CSV)",
                new ImportRequestsAction(requestService));
        requestsBusiness.add("Экспорт запросов (CSV)",
                new ExportRequestsAction(requestService));

        // ЗАПРОСЫ: отчёты
        Menu requestsReports = new Menu("Отчёты по запросам");
        requestsReports.add("Невыполненные запросы",
                new ShowUnresolvedRequestsAction(requestReportService));
        requestsReports.add("Выполненные запросы",
                new ShowResolvedRequestsAction(requestReportService));
        requestsReports.add("Сортировать по количеству ожиданий",
                new SortRequestsByCountAction(requestReportService));
        requestsReports.add("Сортировать по названию книги",
                new SortRequestsByTitleAction(requestReportService));

        Menu requestsMenu = new Menu("Запросы");
        requestsMenu.add("Бизнес-операции", requestsBusiness);
        requestsMenu.add("Отчёты по запросам", requestsReports);

        // КОРНЕВОЕ МЕНЮ
        Menu root = new Menu("Главное меню");
        root.add("Книги", booksMenu);
        root.add("Покупатели", customersMenu);
        root.add("Заказы", ordersMenu);
        root.add("Запросы", requestsMenu);

        return root;
    }
}
