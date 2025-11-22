package task3_4.app;

import task3_4.controller.ui.MenuController;
import task3_4.exceptions.app.AppInitializationException;
import task3_4.view.core.Builder;
import task3_4.view.core.Navigator;
import task3_4.view.factory.DefaultUiActionFactory;
import task3_4.view.factory.IUiActionFactory;
import task3_4.view.util.ConsoleView;

import task3_4.features.books.*;
import task3_4.features.customers.*;
import task3_4.features.orders.*;
import task3_4.features.requests.*;
import task3_4.features.reports.*;

public final class App {
    private App() {}

    public static void run() {

        BookRepository bookRepo = new BookRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        OrderRepository orderRepo = new OrderRepository();
        RequestRepository requestRepo = new RequestRepository();

        BookService bookService = new BookService(bookRepo);
        CustomerService customerService = new CustomerService(customerRepo);
        RequestService requestService = new RequestService(requestRepo, bookRepo, orderRepo);
        OrderService orderService = new OrderService(orderRepo, requestService, customerService, bookService);

        BookReportService bookReportService = new BookReportService(bookService);
        OrderReportService orderReportService = new OrderReportService(orderService);
        RequestReportService requestReportService = new RequestReportService(requestService);

        try {
            AppInitializer.initialize(
                    bookService,
                    customerService,
                    orderService,
                    requestService
            );
        } catch (AppInitializationException e) {

            ConsoleView.warn("КРИТИЧЕСКАЯ ОШИБКА ЗАПУСКА ПРИЛОЖЕНИЯ");
            ConsoleView.warn(e.getMessage());

            if (e.getCause() != null) {
                ConsoleView.warn("Причина: " + e.getCause().getMessage());
            }

            ConsoleView.warn("Приложение будет завершено.");
            return;
        }

        IUiActionFactory factory = new DefaultUiActionFactory(
                bookService,
                customerService,
                orderService,
                requestService,
                bookReportService,
                orderReportService,
                requestReportService
        );

        Builder builder = new Builder().buildMenu(factory.buildRootMenu());

        MenuController controller = new MenuController(builder, new Navigator());
        controller.run();
    }
}