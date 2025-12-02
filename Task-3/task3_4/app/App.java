package task3_4.app;

import task3_4.app.state.AppState;
import task3_4.app.state.BinaryStateStorage;
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

        BinaryStateStorage storage = new BinaryStateStorage();
        AppState loadedState = storage.loadState();

        if (loadedState != null) {
            ConsoleView.info("Восстанавливаю состояние из файла...");

            bookRepo.restoreState(loadedState.getBookState());
            customerRepo.restoreState(loadedState.getCustomerState());
            orderRepo.restoreState(loadedState.getOrderState());
            requestRepo.restoreState(loadedState.getRequestState());

            ConsoleView.ok("Состояние восстановлено успешно!");
        }

        RequestService requestService = new RequestService(requestRepo, bookRepo, orderRepo);
        BookService bookService = new BookService(bookRepo, requestService);
        CustomerService customerService = new CustomerService(customerRepo);
        OrderService orderService = new OrderService(orderRepo, requestService, customerService, bookService);

        BookReportService bookReportService = new BookReportService(bookService);
        OrderReportService orderReportService = new OrderReportService(orderService);
        RequestReportService requestReportService = new RequestReportService(requestService);

        if (loadedState == null) {
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
        AppState newState = new AppState(
                bookRepo.findAllBooks(),
                customerRepo.findAllCustomers(),
                orderRepo.findAllOrders(),
                requestRepo.findAllRequests()
        );

        storage.saveState(newState);

        ConsoleView.ok("Работа завершена. Состояние сохранено.");
    }
}