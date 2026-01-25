package config.processor;

import config.annotation.ConfigProperty;
import di.annotation.Component;
import di.annotation.Singleton;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Properties;

@Component
@Singleton
public class ConfigProcessor {

    public void configure(Object target, String configFile) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {

            if (!field.isAnnotationPresent(ConfigProperty.class)) {
                continue;
            }

            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

            String fileName = annotation.configFileName().isBlank()
                    ? configFile
                    : annotation.configFileName();

            Properties props = loadProperties(fileName);

            String propName = annotation.propertyName().isBlank()
                    ? clazz.getSimpleName() + "." + field.getName()
                    : annotation.propertyName();

            String rawValue = props.getProperty(propName);
            if (rawValue == null) continue;

            rawValue = rawValue.trim();

            Class<?> targetType = resolveTargetType(field, annotation);
            Object convertedValue = convert(rawValue, targetType);

            try {
                field.setAccessible(true);
                field.set(target, convertedValue);
            } catch (Exception e) {
                throw new RuntimeException("Ошибка установки поля " + field.getName(), e);
            }
        }
    }

    private Properties loadProperties(String fileName) {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Конфигурационный файл не найден: " + fileName);
            }

            Properties props = new Properties();
            props.load(input);
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки config-файла: " + fileName, e);
        }
    }

    private Class<?> resolveTargetType(Field field, ConfigProperty annotation) {

        if (annotation.type() != Object.class) {
            return annotation.type();
        }

        Class<?> fieldType = field.getType();

        if (Collection.class.isAssignableFrom(fieldType)) {
            if (field.getGenericType() instanceof ParameterizedType) {
                return String.class;
            }
        }

        if (fieldType == Object.class) {
            throw new RuntimeException(
                    "Поле типа Object требует указания type в @ConfigProperty: "
                            + field.getDeclaringClass().getSimpleName() + "." + field.getName()
            );
        }
        return fieldType;
    }

    private Object convert(String raw, Class<?> targetType) {
        if (targetType == String.class) return raw;
        if (targetType == int.class || targetType == Integer.class) return Integer.parseInt(raw);
        if (targetType == boolean.class || targetType == Boolean.class) return Boolean.parseBoolean(raw);
        if (targetType == long.class || targetType == Long.class) return Long.parseLong(raw);
        if (targetType == double.class || targetType == Double.class) return Double.parseDouble(raw);

        throw new RuntimeException("Неизвестный тип: " + targetType.getName());
    }
}
