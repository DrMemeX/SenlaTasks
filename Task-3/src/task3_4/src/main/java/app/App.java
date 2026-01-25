package app;

import config.BookstoreConfig;
import controller.ui.MenuController;
import di.processor.DependencyInjector;
import exceptions.app.AppInitializationException;
import features.books.BookRepository;
import features.books.BookService;
import features.customers.CustomerRepository;
import features.customers.CustomerService;
import features.orders.OrderRepository;
import features.orders.OrderService;
import features.reports.BookReportService;
import features.reports.OrderReportService;
import features.reports.RequestReportService;
import features.requests.RequestRepository;
import features.requests.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.core.Builder;
import view.core.Navigator;
import view.factory.DefaultUiActionFactory;
import view.factory.IUiActionFactory;
import view.util.ConsoleView;

public final class App {
    private App() {
    }

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void run() {
        log.info("APP_START");

        try {
            DependencyInjector injector = new DependencyInjector();
            log.info("DI_CREATED injector={}", injector.getClass().getSimpleName());

            BookstoreConfig config = injector.create(BookstoreConfig.class);
            log.info("CONFIG_LOADED autoResolveRequests={} oldBookMonths={}",
                    config.isAutoResolveRequestsEnabled(),
                    config.getOldBookMonths());

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
            log.info("REPOSITORIES_CREATED bookRepo={} customerRepo={} orderRepo={} requestRepo={}",
                    bookRepo.getClass().getSimpleName(),
                    customerRepo.getClass().getSimpleName(),
                    orderRepo.getClass().getSimpleName(),
                    requestRepo.getClass().getSimpleName());

            BookService bookService = injector.create(BookService.class);
            CustomerService customerService = injector.create(CustomerService.class);
            OrderService orderService = injector.create(OrderService.class);
            RequestService requestService = injector.create(RequestService.class);
            log.info("SERVICES_CREATED bookService={} customerService={} orderService={} requestService={}",
                    bookService.getClass().getSimpleName(),
                    customerService.getClass().getSimpleName(),
                    orderService.getClass().getSimpleName(),
                    requestService.getClass().getSimpleName());

            BookReportService bookReportService = injector.create(BookReportService.class);
            OrderReportService orderReportService = injector.create(OrderReportService.class);
            RequestReportService requestReportService = injector.create(RequestReportService.class);
            log.info("REPORT_SERVICES_CREATED bookReportService={} orderReportService={} requestReportService={}",
                    bookReportService.getClass().getSimpleName(),
                    orderReportService.getClass().getSimpleName(),
                    requestReportService.getClass().getSimpleName());

            boolean dbHasData;
            try {
                dbHasData =
                        !bookService.getAllBooks().isEmpty()
                                || !customerService.getAllCustomers().isEmpty()
                                || !orderService.getAllOrders().isEmpty()
                                || !requestService.getAllRequests().isEmpty();

                log.info("DB_CHECK_DONE dbHasData={}", dbHasData);
            } catch (Exception e) {
                log.error("DB_CONNECTION_ERROR during=db_check message={}", e.getMessage(), e);
                ConsoleView.warn("Нет подключения к БД. Проверь db.properties (url/user/password) и доступность Postgres.");
                log.info("APP_STOP reason=db_unavailable");
                return;
            }

            if (!dbHasData) {
                ConsoleView.title("Инициализация тестовых данных");
                log.info("INIT_DATA_START");

                try {
                    AppInitializer initializer = injector.create(AppInitializer.class);
                    initializer.initialize();

                    ConsoleView.ok("Тестовые данные загружены.");
                    log.info("INIT_DATA_SUCCESS");
                } catch (AppInitializationException e) {
                    log.error("INIT_DATA_ERROR message={}", e.getMessage(), e);

                    ConsoleView.warn("КРИТИЧЕСКАЯ ОШИБКА ЗАПУСКА ПРИЛОЖЕНИЯ");
                    ConsoleView.warn(e.getMessage());

                    if (e.getCause() != null) {
                        ConsoleView.warn("Причина: " + e.getCause().getMessage());
                    }

                    ConsoleView.warn("Приложение будет завершено.");
                    log.info("APP_STOP reason=init_error");
                    return;
                }
            } else {
                ConsoleView.info("Данные уже есть в БД — пропускаю инициализацию.");
                ConsoleView.hr();
                log.info("INIT_DATA_SKIPPED reason=db_has_data");
            }

            log.info("UI_BUILD_START");
            IUiActionFactory factory = injector.create(DefaultUiActionFactory.class);
            Builder builder = new Builder().buildMenu(factory.buildRootMenu());
            log.info("UI_BUILD_DONE rootMenu={}", builder.getRootMenu() != null ? builder.getRootMenu().getName() : "null");

            log.info("UI_RUN_START");
            MenuController controller = new MenuController(builder, new Navigator());
            controller.run();
            log.info("UI_RUN_END");

            ConsoleView.ok("Работа завершена.");
            log.info("APP_SUCCESS");
        } catch (Exception e) {
            log.error("APPLICATION_STARTUP_ERROR message={}", e.getMessage(), e);
            ConsoleView.warn("Критическая ошибка запуска приложения: " + e.getMessage());
        }
    }
}
