package task3_4.app;

import di_module.di_processor.DependencyInjector;
import task3_4.app.state.AppState;
import task3_4.app.state.BinaryStateStorage;
import task3_4.config.BookstoreConfig;
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

        DependencyInjector injector = new DependencyInjector();

        BookstoreConfig config = injector.create(BookstoreConfig.class);

        ConsoleView.title("Текущие настройки магазина");
        ConsoleView.info("Авто-выполнение запросов: " +
                (config.isAutoResolveRequestsEnabled() ? "ВКЛЮЧЕНО" : "ВЫКЛЮЧЕНО"));
        ConsoleView.info("Книга считается «залежавшейся» через " +
                config.getOldBookMonths() + " мес.");
        ConsoleView.hr();

        BookRepository bookRepo = injector.create(BookRepository.class);
        CustomerRepository customerRepo = injector.create(CustomerRepository.class);
        OrderRepository orderRepo = injector.create(OrderRepository.class);
        RequestRepository requestRepo = injector.create(RequestRepository.class);

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

        BookService bookService = injector.create(BookService.class);
        CustomerService customerService = injector.create(CustomerService.class);
        OrderService orderService = injector.create(OrderService.class);
        RequestService requestService = injector.create(RequestService.class);

        BookReportService bookReportService = injector.create(BookReportService.class);
        OrderReportService orderReportService = injector.create(OrderReportService.class);
        RequestReportService requestReportService = injector.create(RequestReportService.class);

        if (loadedState == null) {
            try {
                AppInitializer initializer = injector.create(AppInitializer.class);
                initializer.initialize();
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

        IUiActionFactory factory = injector.create(DefaultUiActionFactory.class);

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