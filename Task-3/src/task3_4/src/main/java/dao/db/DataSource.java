package dao.db;

import exceptions.dao.DbConnectionException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DataSource {

    private static volatile DataSource instance;

    private static final String PROPS_FILE = "db.properties";

    private static final String KEY_URL = "db.url";
    private static final String KEY_USER = "db.user";
    private static final String KEY_PASSWORD = "db.password";
    private static final String KEY_DRIVER = "db.driver";

    private final Properties props;

    private DataSource() {
        this.props = loadProps();

        String driver = props.getProperty(KEY_DRIVER);
        if (driver != null && !driver.isBlank()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new DbConnectionException("JDBC driver not found: " + driver, e);
            }
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    props.getProperty(KEY_URL),
                    props.getProperty(KEY_USER),
                    props.getProperty(KEY_PASSWORD)
            );
        } catch (Exception e) {
            throw new DbConnectionException("Не удалось создать подключение к БД", e);
        }
    }

    private Properties loadProps() {
        try (InputStream is = DataSource.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
            if (is == null) {
                throw new DbConnectionException("Файл настроек не найден в ресурсах: " + PROPS_FILE);
            }
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (Exception e) {
            throw new DbConnectionException("Не удалось загрузить настройки из файла: " + PROPS_FILE, e);
        }
    }
}
