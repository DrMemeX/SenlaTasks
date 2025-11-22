package task3_4.features.orders;

import task3_4.exceptions.csv.CsvException;
import task3_4.features.books.Book;
import task3_4.features.customers.Customer;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCsvExporter {

    public void exportTo(String filePath, List<Order> orders) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Order o : orders) {

                long customerId = getCustomerId(o.getCustomer());

                String creationDateStr =
                        o.getCreationDate() != null ? o.getCreationDate().toString() : "-";

                String completionDateStr =
                        o.getCompletionDate() != null ? o.getCompletionDate().toString() : "-";

                String bookIdsStr = toBookIdsString(o.getOrderedBooks());

                pw.printf("%d;%d;%s;%s;%s;%.2f;%s%n",
                        o.getId(),
                        customerId,
                        o.getStatus(),
                        creationDateStr,
                        completionDateStr,
                        o.getTotalPrice(),
                        bookIdsStr
                );
            }

        } catch (Exception e) {
            throw new CsvException("Ошибка экспорта заказов в CSV: " + filePath, e);
        }
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
