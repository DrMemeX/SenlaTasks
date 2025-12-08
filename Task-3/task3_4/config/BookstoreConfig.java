package task3_4.config;

import config_module.config_annotation.ConfigProperty;
import config_module.config_processor.ConfigProcessor;
import di_module.di_annotation.Component;
import di_module.di_annotation.Init;
import di_module.di_annotation.Inject;
import di_module.di_annotation.Singleton;
import task3_4.exceptions.config.ConfigException;
import task3_4.exceptions.config.ConfigFileNotFoundException;
import task3_4.view.util.ConsoleView;

@Component
@Singleton
public final class BookstoreConfig {

    private static final String CONFIG_FILE = "task3_4/config.properties";

    private static final int DEFAULT_OLD_BOOK_MONTHS = 6;
    private static final boolean DEFAULT_AUTO_RESOLVE_REQUESTS = true;

    @Inject
    private ConfigProcessor configProcessor;

    @ConfigProperty(propertyName = "bookstore.oldBookMonths", type = String.class)
    private String oldBookMonthsRaw;

    @ConfigProperty(propertyName = "bookstore.autoResolveRequests", type = String.class)
    private String autoResolveRequestsRaw;

    private int oldBookMonths;
    private boolean autoResolveRequestsEnabled;

    private BookstoreConfig() {
    }

    @Init
    public void init() {
        try {
            configProcessor.configure(this, CONFIG_FILE);
        } catch (RuntimeException e) {
            ConfigFileNotFoundException ex =
                    new ConfigFileNotFoundException(CONFIG_FILE);
            ConsoleView.warn(ex.getMessage());
        }

        this.oldBookMonths = parseOldBookMonths();
        this.autoResolveRequestsEnabled = parseAutoResolveRequests();
    }

    private int parseOldBookMonths() {
        String raw = oldBookMonthsRaw;

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

    private boolean parseAutoResolveRequests() {
        String raw = autoResolveRequestsRaw;

        if (raw == null || raw.isBlank()) {
            ConfigException ex = new ConfigException(
                    "Отсутствует обязательное свойство 'bookstore.autoResolveRequests' в " + CONFIG_FILE +
                            ". Используется значение по умолчанию: " + DEFAULT_AUTO_RESOLVE_REQUESTS
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

    public int getOldBookMonths() {
        return oldBookMonths;
    }

    public boolean isAutoResolveRequestsEnabled() {
        return autoResolveRequestsEnabled;
    }
}
