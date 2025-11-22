package task3_4.features.requests;

import task3_4.exceptions.csv.CsvException;
import task3_4.features.orders.Order;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class RequestCsvExporter {

    public void exportTo(String filePath, List<Request> requests) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Request r : requests) {

                String ordersStr = toOrderIdString(r.getWaitingOrders());

                pw.printf("%d;%d;%s;%s%n",
                        r.getId(),
                        r.getBook().getId(),
                        r.isResolved(),
                        ordersStr
                );
            }

        } catch (Exception e) {
            throw new CsvException("Ошибка экспорта запросов в CSV: " + filePath, e);
        }
    }

    private String toOrderIdString(List<Order> orders) {
        if (orders == null || orders.isEmpty()) return "";
        return orders.stream()
                .map(o -> Long.toString(o.getId()))
                .collect(Collectors.joining(","));
    }
}
