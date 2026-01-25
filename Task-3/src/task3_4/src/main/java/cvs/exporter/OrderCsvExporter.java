package cvs.exporter;

import features.books.Book;
import features.customers.Customer;
import features.orders.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCsvExporter extends AbstractCsvExporter<Order> {

    @Override
    protected String format(Order o) {

        long customerId = getCustomerId(o.getCustomer());

        String creationDateStr =
                o.getCreationDate() != null ? o.getCreationDate().toString() : "-";

        String completionDateStr =
                o.getCompletionDate() != null ? o.getCompletionDate().toString() : "-";

        String bookIdsStr = toBookIdsString(o.getOrderedBooks());

        return String.format("%d;%d;%s;%s;%s;%.2f;%s",
                o.getId(),
                customerId,
                o.getStatus(),
                creationDateStr,
                completionDateStr,
                o.getTotalPrice(),
                bookIdsStr
        );
    }

    @Override
    protected String buildErrorMessage(String filePath) {
        return "Ошибка экспорта заказов в CSV: " + filePath;
    }

    private long getCustomerId(Customer customer) {
        return customer != null ? customer.getId() : -1L;
    }

    private String toBookIdsString(List<Book> orderedBooks) {
        if (orderedBooks == null || orderedBooks.isEmpty()) {
            return "";
        }
        return orderedBooks.stream()
                .map(b -> Long.toString(b.getId()))
                .collect(Collectors.joining(","));
    }
}
