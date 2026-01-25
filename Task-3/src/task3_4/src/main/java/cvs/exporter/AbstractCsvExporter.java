package cvs.exporter;

import exceptions.csv.CsvException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public abstract class AbstractCsvExporter<E> {

    public final void exportTo(String filePath, List<E> items) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            for (E item : items) {
                String line = format(item);
                pw.println(line);
            }
        } catch (Exception e) {
            throw new CsvException(buildErrorMessage(filePath), e);
        }
    }

    protected abstract String format(E entity);

    protected abstract String buildErrorMessage(String filePath);
}

