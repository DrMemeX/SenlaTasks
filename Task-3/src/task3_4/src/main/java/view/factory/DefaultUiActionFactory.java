package view.factory;

import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import features.books.BookService;
import features.customers.CustomerService;
import features.orders.OrderService;
import features.reports.BookReportService;
import features.reports.OrderReportService;
import features.reports.RequestReportService;
import features.requests.RequestService;
import view.menu.Menu;

import view.action.books.buisness.AddBookAction;
import view.action.books.buisness.DeleteBookAction;
import view.action.books.buisness.ExportBooksAction;
import view.action.books.buisness.ImportBooksAction;
import view.action.books.buisness.UpdateBookAction;

import view.action.books.reports.BookDescriptionByTitleAction;
import view.action.books.reports.BookDescriptionByTitleAndAuthorAction;
import view.action.books.reports.BooksSortByAvailabilityAction;
import view.action.books.reports.BooksSortByPriceAction;
import view.action.books.reports.BooksSortByReleaseDateAction;
import view.action.books.reports.BooksSortByTitleAction;
import view.action.books.reports.OldBooksSortByDateAction;
import view.action.books.reports.OldBooksSortByPriceAction;

import view.action.customers.buisness.AddCustomerAction;
import view.action.customers.buisness.DeleteCustomerAction;
import view.action.customers.buisness.ExportCustomersAction;
import view.action.customers.buisness.ImportCustomersAction;
import view.action.customers.buisness.UpdateCustomerAction;

import view.action.orders.buisness.CancelOrderAction;
import view.action.orders.buisness.CompleteOrderAction;
import view.action.orders.buisness.CreateOrderAction;
import view.action.orders.buisness.DeleteOrderAction;
import view.action.orders.buisness.ExportOrdersAction;
import view.action.orders.buisness.ImportOrdersAction;
import view.action.orders.buisness.UpdateOrderStatusAction;

import view.action.orders.reports.CompletedOrdersByDateAction;
import view.action.orders.reports.CompletedOrdersByPriceAction;
import view.action.orders.reports.CompletedOrdersCountAction;
import view.action.orders.reports.OrderDetailsAction;
import view.action.orders.reports.OrdersSortByDateAction;
import view.action.orders.reports.OrdersSortByPriceAction;
import view.action.orders.reports.OrdersSortByStatusAction;
import view.action.orders.reports.TotalIncomeAction;

import view.action.requests.buisness.CompleteRequestAction;
import view.action.requests.buisness.DeleteRequestAction;
import view.action.requests.buisness.ExportRequestsAction;
import view.action.requests.buisness.ImportRequestsAction;

import view.action.requests.reports.ShowResolvedRequestsAction;
import view.action.requests.reports.ShowUnresolvedRequestsAction;
import view.action.requests.reports.SortRequestsByCountAction;
import view.action.requests.reports.SortRequestsByTitleAction;


@Component
@Singleton
public class DefaultUiActionFactory implements IUiActionFactory {

    @Inject
    private BookService bookService;
    @Inject
    private CustomerService customerService;
    @Inject
    private OrderService orderService;
    @Inject
    private RequestService requestService;
    @Inject
    private BookReportService bookReportService;
    @Inject
    private OrderReportService orderReportService;
    @Inject
    private RequestReportService requestReportService;

    public DefaultUiActionFactory() {
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
