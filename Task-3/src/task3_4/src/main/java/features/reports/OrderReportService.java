package features.reports;

import di.annotation.Component;
import di.annotation.Inject;
import di.annotation.Singleton;
import common.status.OrderStatus;
import features.books.Book;
import features.customers.Customer;
import features.orders.Order;
import features.orders.OrderService;
import view.util.ConsoleView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Singleton
public class OrderReportService {

    @Inject
    private OrderService orderService;

    public OrderReportService() {
    }

    public void showOrdersSortedByCompletionDate(boolean ascending) {
        List<Order> list = orderService.getAllOrders();

        Comparator<Order> cmp = Comparator.comparing(
                Order::getCompletionDate,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
        if (!ascending) cmp = cmp.reversed();

        List<Order> sorted = list.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Заказы по дате исполнения (старые → новые)"
                : "Заказы по дате исполнения (новые → старые)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Заказов нет.");
            return;
        }

        for (Order o : sorted) {
            ConsoleView.info(o.toString());
        }
    }

    public void showOrdersSortedByPrice(boolean ascending) {
        List<Order> list = orderService.getAllOrders();

        Comparator<Order> cmp = Comparator.comparing(Order::getTotalPrice);
        if (!ascending) cmp = cmp.reversed();

        List<Order> sorted = list.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Заказы по сумме (меньше → больше)"
                : "Заказы по сумме (больше → меньше)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Заказов нет.");
            return;
        }

        for (Order o : sorted) ConsoleView.info(o.toString());
    }

    public void showOrdersSortedByStatus(boolean ascending) {
        List<Order> list = orderService.getAllOrders();

        Comparator<Order> cmp = Comparator.comparing(Order::getStatus);
        if (!ascending) cmp = cmp.reversed();

        List<Order> sorted = list.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title(ascending
                ? "Заказы по статусу (NEW → COMPLETED → CANCELLED)"
                : "Заказы по статусу (CANCELLED → COMPLETED → NEW)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Заказов нет.");
            return;
        }

        for (Order o : sorted) ConsoleView.info(o.toString());
    }

    public void showCompletedOrdersByDate(LocalDate start, LocalDate end, boolean ascending) {
        List<Order> completed = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .filter(o -> o.getCompletionDate() != null)
                .filter(o -> !o.getCompletionDate().isBefore(start)
                        && !o.getCompletionDate().isAfter(end))
                .collect(Collectors.toList());

        Comparator<Order> cmp = Comparator.comparing(Order::getCompletionDate);
        if (!ascending) cmp = cmp.reversed();

        List<Order> sorted = completed.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title("Выполненные заказы за период (дата)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Нет выполненных заказов в выбранном периоде.");
            return;
        }

        for (Order o : sorted) ConsoleView.info(o.toString());
    }

    public void showCompletedOrdersByPrice(LocalDate start, LocalDate end, boolean ascending) {
        List<Order> completed = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .filter(o -> o.getCompletionDate() != null)
                .filter(o -> !o.getCompletionDate().isBefore(start)
                        && !o.getCompletionDate().isAfter(end))
                .collect(Collectors.toList());

        Comparator<Order> cmp = Comparator.comparing(Order::getTotalPrice);
        if (!ascending) cmp = cmp.reversed();

        List<Order> sorted = completed.stream().sorted(cmp).collect(Collectors.toList());

        ConsoleView.title("Выполненные заказы за период (цена)");

        if (sorted.isEmpty()) {
            ConsoleView.warn("Нет выполненных заказов в выбранном периоде.");
            return;
        }

        for (Order o : sorted) ConsoleView.info(o.toString());
    }

    public void showTotalIncome(LocalDate start, LocalDate end) {
        double total = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .filter(o -> o.getCompletionDate() != null)
                .filter(o -> !o.getCompletionDate().isBefore(start)
                        && !o.getCompletionDate().isAfter(end))
                .mapToDouble(Order::getTotalPrice)
                .sum();

        ConsoleView.title("Суммарный доход за период");
        ConsoleView.info("Период: " + start + " — " + end);
        ConsoleView.ok("Заработано: " + total + " руб.");
    }

    public void showCompletedOrdersCount(LocalDate start, LocalDate end) {
        long count = orderService.getAllOrders().stream()
                .filter(o -> o.getStatus() == OrderStatus.COMPLETED)
                .filter(o -> o.getCompletionDate() != null)
                .filter(o -> !o.getCompletionDate().isBefore(start)
                        && !o.getCompletionDate().isAfter(end))
                .count();

        ConsoleView.title("Количество выполненных заказов за период");
        ConsoleView.info("Период: " + start + " — " + end);
        ConsoleView.ok("Количество: " + count);
    }

    public void showOrderDetails(Order order) {
        if (order == null) {
            ConsoleView.warn("Заказ не найден.");
            return;
        }

        ConsoleView.title("Детали заказа №" + order.getId());

        ConsoleView.info("Статус: " + order.getStatus());
        ConsoleView.info("Создан: " + order.getCreationDate());
        ConsoleView.info("Завершён: " + (order.getCompletionDate() != null
                ? order.getCompletionDate()
                : "—"));

        ConsoleView.hr();
        ConsoleView.info("Сумма: " + order.getTotalPrice() + " руб.");

        ConsoleView.hr();
        ConsoleView.info("Заказчик:");

        Customer c = order.getCustomer();
        if (c == null) {
            ConsoleView.warn("  — данные не указаны");
        } else {
            ConsoleView.info("  Имя: " + c.getName());
            ConsoleView.info("  Телефон: " + c.getPhone());
            ConsoleView.info("  Email: " + c.getEmail());
            ConsoleView.info("  Адрес: " + c.getAddress());
        }

        ConsoleView.hr();
        ConsoleView.info("Книги:");

        for (Book b : order.getOrderedBooks()) {
            ConsoleView.info(
                    "  " + b.getTitle()
                            + " | " + b.getPrice() + " руб."
                            + " | " + b.getStatus()
            );
        }
    }

    public void showOrderDetails(long id) {
        Order o = orderService.getOrderById(id);
        showOrderDetails(o);
    }
}
