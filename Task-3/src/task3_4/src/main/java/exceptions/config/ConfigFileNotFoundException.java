package exceptions.config;

public class ConfigFileNotFoundException extends ConfigException {

    public ConfigFileNotFoundException(String filePath) {
        super("Файл конфигурации не найден: " + filePath);
    }
}
