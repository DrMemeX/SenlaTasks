package task3_4.features.customers;


import task3_4.exceptions.csv.CsvException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class CustomerCsvExporter {

    public void exportTo(String filePath, List<Customer> customers) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (Customer c : customers) {
                pw.printf("%d;%s;%s;%s;%s%n",
                        c.getId(),
                        c.getName(),
                        c.getPhone(),
                        c.getEmail(),
                        c.getAddress()
                );
            }

        } catch (Exception e) {
            throw new CsvException("Ошибка экспорта клиентов в CSV: " + filePath, e);
        }
    }
}