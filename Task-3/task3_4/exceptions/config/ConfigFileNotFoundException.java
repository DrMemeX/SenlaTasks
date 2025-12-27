package task3_4.exceptions.config;

public class ConfigFileNotFoundException extends ConfigException {

    public ConfigFileNotFoundException(String filePath) {
        super("Файл конфигурации не найден: " + filePath);
    }
}
