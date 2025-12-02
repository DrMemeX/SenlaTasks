package task3_4.config;

import task3_4.exceptions.config.ConfigException;
import task3_4.exceptions.config.ConfigFileNotFoundException;
import task3_4.view.util.ConsoleView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BookstoreConfig {

    private static final String CONFIG_FILE = "task3_4/config.properties";
    private static final BookstoreConfig INSTANCE = new BookstoreConfig();

    private final Properties props = new Properties();

    private static final int DEFAULT_OLD_BOOK_MONTHS = 6;
    private static final boolean DEFAULT_AUTO_RESOLVE_REQUESTS = true;

    private BookstoreConfig() {
        load();
    }

    public static BookstoreConfig get() {
        return INSTANCE;
    }

    private void load() {
        try (InputStream in = BookstoreConfig.class
                .getClassLoader()
                .getResourceAsStream("task3_4/config.properties")) {

            if (in == null) {
                ConfigFileNotFoundException ex =
                        new ConfigFileNotFoundException("task3_4/config.properties");
                ConsoleView.warn(ex.getMessage());
                return;
            }

            props.load(in);

        } catch (IOException e) {
            ConfigException ex =
                    new ConfigException("Ошибка чтения файла конфигурации: task3_4/config.properties", e);
            ConsoleView.warn(ex.getMessage());
        }
    }

    public int getOldBookMonths() {
        String raw = props.getProperty("bookstore.oldBookMonths");

        if (raw == null || raw.isBlank()) {
            ConfigException ex = new ConfigException(
                    "Отсутствует обязательное свойство 'bookstore.oldBookMonths' в " + CONFIG_FILE +
                            ". Используется значение по умолчанию: " + DEFAULT_OLD_BOOK_MONTHS
            );
            ConsoleView.warn(ex.getMessage());
            return DEFAULT_OLD_BOOK_MONTHS;
        }

        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            ConfigException ex = new ConfigException(
                    "Некорректное значение свойства 'bookstore.oldBookMonths': " + raw +
                            ". Ожидается целое число. Используется значение по умолчанию: " +
                            DEFAULT_OLD_BOOK_MONTHS, e
            );
            ConsoleView.warn(ex.getMessage());
            return DEFAULT_OLD_BOOK_MONTHS;
        }
    }

    public boolean isAutoResolveRequestsEnabled() {
        String raw = props.getProperty("bookstore.autoResolveRequests");

        if (raw == null || raw.isBlank()) {
            ConfigException ex = new ConfigException(
                    "Отсутствует обязательное свойство 'bookstore.autoResolveRequests' в " + CONFIG_FILE +
                            ". Используется значение по умолчанию: " +
                            DEFAULT_AUTO_RESOLVE_REQUESTS
            );
            ConsoleView.warn(ex.getMessage());
            return DEFAULT_AUTO_RESOLVE_REQUESTS;
        }

        String normalized = raw.trim().toLowerCase();

        if (normalized.equals("true") || normalized.equals("false")) {
            return Boolean.parseBoolean(normalized);
        }

        ConfigException ex = new ConfigException(
                "Некорректное значение свойства 'bookstore.autoResolveRequests': " + raw +
                        ". Ожидается true/false. Используется значение по умолчанию: " +
                        DEFAULT_AUTO_RESOLVE_REQUESTS
        );
        ConsoleView.warn(ex.getMessage());
        return DEFAULT_AUTO_RESOLVE_REQUESTS;
    }
}
