package task3_4.app;

import di_module.di_processor.DependencyInjector;
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

        BookService bookService = injector.create(BookService.class);
        CustomerService customerService = injector.create(CustomerService.class);
        OrderService orderService = injector.create(OrderService.class);
        RequestService requestService = injector.create(RequestService.class);

        BookReportService bookReportService = injector.create(BookReportService.class);
        OrderReportService orderReportService = injector.create(OrderReportService.class);
        RequestReportService requestReportService = injector.create(RequestReportService.class);


        boolean dbHasData =
                !bookService.getAllBooks().isEmpty()
                        || !customerService.getAllCustomers().isEmpty()
                        || !orderService.getAllOrders().isEmpty()
                        || !requestService.getAllRequests().isEmpty();

        if (!dbHasData) {
            ConsoleView.title("Инициализация тестовых данных");
            try {
                AppInitializer initializer = injector.create(AppInitializer.class);
                initializer.initialize();
                ConsoleView.ok("Тестовые данные загружены.");
            } catch (AppInitializationException e) {

                ConsoleView.warn("КРИТИЧЕСКАЯ ОШИБКА ЗАПУСКА ПРИЛОЖЕНИЯ");
                ConsoleView.warn(e.getMessage());

                if (e.getCause() != null) {
                    ConsoleView.warn("Причина: " + e.getCause().getMessage());
                }

                ConsoleView.warn("Приложение будет завершено.");
                return;
            }
        } else {
            ConsoleView.info("Данные уже есть в БД — пропускаю инициализацию.");
            ConsoleView.hr();
        }

        IUiActionFactory factory = injector.create(DefaultUiActionFactory.class);

        Builder builder = new Builder().buildMenu(factory.buildRootMenu());

        MenuController controller = new MenuController(builder, new Navigator());
        controller.run();

        ConsoleView.ok("Работа завершена.");
    }
}
