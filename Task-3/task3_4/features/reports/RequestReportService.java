package task3_4.features.reports;

import di_module.di_annotation.Component;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.features.books.Book;
import task3_4.features.orders.Order;
import task3_4.features.requests.Request;
import task3_4.features.requests.RequestService;
import task3_4.view.util.ConsoleView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Singleton
public class RequestReportService {

    @Inject
    private RequestService requestService;

    public RequestReportService() {
    }

    public void showResolvedRequests() {
        List<Request> resolved = requestService.getAllRequests().stream()
                .filter(Request::isResolved)
                .collect(Collectors.toList());

        ConsoleView.title("Выполненные запросы");

        if (resolved.isEmpty()) {
            ConsoleView.warn("Выполненных запросов нет.");
            return;
        }

        for (Request r : resolved) {
            Book b = r.getBook();
            ConsoleView.info(
                    "ID: " + r.getId() +
                            " | Книга: " + (b != null ? b.getTitle() : "<не указана>")
            );
        }
    }

    public void showUnresolvedRequests() {
        List<Request> unresolved = requestService.getAllRequests().stream()
                .filter(r -> !r.isResolved())
                .collect(Collectors.toList());

        ConsoleView.title("Невыполненные запросы");

        if (unresolved.isEmpty()) {
            ConsoleView.warn("Все запросы выполнены.");
            return;
        }

        for (Request r : unresolved) {
            Book b = r.getBook();
            int waiting = r.getWaitingOrders().size();
            ConsoleView.info(
                    "ID: " + r.getId() +
                            " | Книга: " + (b != null ? b.getTitle() : "<не указана>") +
                            " | Ожидают заказов: " + waiting
            );
        }
    }

    public void showRequestsSortedByCount(boolean ascending) {
        List<Request> requests = requestService.getAllRequests();

        Comparator<Request> cmp =
                Comparator.comparingInt(r -> r.getWaitingOrders().size());
        if (!ascending) {
            cmp = cmp.reversed();
        }

        List<Request> sorted = requests.stream()
                .sorted(cmp)
                .collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Запросы по количеству ожиданий (меньше → больше)"
                : "Запросы по количеству ожиданий (больше → меньше)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Запросов пока нет.");
            return;
        }

        for (Request r : sorted) {
            Book b = r.getBook();
            int count = r.getWaitingOrders().size();
            ConsoleView.info(
                    "Книга: " + (b != null ? b.getTitle() : "<не указана>") +
                            " — ожиданий: " + count
            );
        }
    }

    public void showRequestsSortedByTitle(boolean ascending) {
        List<Request> requests = requestService.getAllRequests();

        Comparator<Request> cmp = Comparator.comparing(
                r -> {
                    Book b = r.getBook();
                    return b != null && b.getTitle() != null ? b.getTitle() : "";
                },
                String.CASE_INSENSITIVE_ORDER
        );

        if (!ascending) {
            cmp = cmp.reversed();
        }

        List<Request> sorted = requests.stream()
                .sorted(cmp)
                .collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Запросы по алфавиту (А → Я)"
                : "Запросы по алфавиту (Я → А)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Запросов пока нет.");
            return;
        }

        for (Request r : sorted) {
            Book b = r.getBook();
            int count = r.getWaitingOrders().size();
            ConsoleView.info(
                    "Книга: " + (b != null ? b.getTitle() : "<не указана>") +
                            " — ожиданий: " + count
            );
        }
    }

    public void showActiveRequests() {
        List<Request> requests = requestService.getAllRequests().stream()
                .filter(r -> !r.isResolved())
                .collect(Collectors.toList());

        ConsoleView.title("Активные запросы");

        if (requests.isEmpty()) {
            ConsoleView.warn("Активных запросов нет.");
            return;
        }

        for (Request r : requests) {
            Book b = r.getBook();
            List<Order> waiting = r.getWaitingOrders();
            ConsoleView.info(
                    "Книга: " + (b != null ? b.getTitle() : "<не указана>") +
                            " — ожидающих заказов: " + waiting.size()
            );
        }
    }
}