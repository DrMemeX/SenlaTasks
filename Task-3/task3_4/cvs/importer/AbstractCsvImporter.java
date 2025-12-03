package task3_4.cvs.importer;

import task3_4.exceptions.csv.CsvException;
import task3_4.exceptions.csv.CsvFileNotFoundException;
import task3_4.exceptions.csv.CsvFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCsvImporter<D> {

    public final List<D> importFrom(String filePath) {

        List<D> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int row = 0;

            while ((line = br.readLine()) != null) {
                row++;

                if (line.isBlank()) continue;

                String[] parts = line.split(";");

                if (parts.length < minColumns()) {
                    throw new CsvFormatException(
                            "Некорректная строка в файле " + filePath +
                                    " [строка " + row + "]: " + errorTooFewColumns(parts.length)
                    );
                }

                D dto = parseLine(parts, row);
                result.add(dto);
            }

        } catch (java.io.FileNotFoundException e) {
            throw new CsvFileNotFoundException(filePath);

        } catch (CsvFormatException e) {
            throw e;

        } catch (Exception e) {
            throw new CsvException(buildReadError(filePath), e);
        }

        return result;
    }

    protected abstract int minColumns();

    protected abstract D parseLine(String[] parts, int row);

    protected abstract String buildReadError(String filePath);

    protected String errorTooFewColumns(int found) {
        return "ожидалось минимум " + minColumns() +
                " колонок, получено " + found;
    }
}

