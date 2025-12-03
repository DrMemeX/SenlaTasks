package task3_4.cvs.exporter;

import task3_4.features.orders.Order;
import task3_4.features.requests.Request;

import java.util.List;
import java.util.stream.Collectors;

public class RequestCsvExporter extends AbstractCsvExporter<Request> {

    @Override
    protected String format(Request r) {

        String ordersStr = toOrderIdString(r.getWaitingOrders());

        return String.format("%d;%d;%s;%s",
                r.getId(),
                r.getBook().getId(),
                r.isResolved(),
                ordersStr
        );
    }

    @Override
    protected String buildErrorMessage(String filePath) {
        return "Ошибка экспорта запросов в CSV: " + filePath;
    }

    private String toOrderIdString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) return "";
        return orders.stream()
                .map(o -> Long.toString(o.getId()))
                .collect(Collectors.joining(","));
    }
}
