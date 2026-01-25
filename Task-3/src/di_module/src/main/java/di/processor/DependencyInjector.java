package di.processor;

import di.annotation.Component;
import di.annotation.Init;
import di.annotation.Inject;
import di.annotation.Singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DependencyInjector {

    private final Map<Class<?>, Object> singletonCache = new HashMap<>();

    public <T> T create(Class<T> type) {
        try {
            if (type.isAnnotationPresent(Singleton.class)) {
                if (singletonCache.containsKey(type)) {
                    return type.cast(singletonCache.get(type));
                }
            }

            if (!type.isAnnotationPresent(Component.class)) {
                throw new RuntimeException("Класс " + type.getName() + " не помечен @Component");
            }

            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            T instance = constructor.newInstance();

            injectDependencies(instance);

            invokeInitMethods(instance);

            if (type.isAnnotationPresent(Singleton.class)) {
                singletonCache.put(type, instance);
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания компонента: " + type.getName(), e);
        }
    }

    public void injectDependencies(Object target) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            Class<?> dependencyType = field.getType();
            Object dependency;

            if (dependencyType.isAnnotationPresent(Singleton.class)) {

                if (singletonCache.containsKey(dependencyType)) {
                    dependency = singletonCache.get(dependencyType);
                } else {
                    dependency = create(dependencyType);
                    singletonCache.put(dependencyType, dependency);
                }
            } else {
                dependency = create(dependencyType);
            }

            try {
                field.setAccessible(true);
                field.set(target, dependency);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Ошибка внедрения зависимости в поле " + field.getName() +
                                " класса " + clazz.getName(), e
                );
            }
        }
    }

    private void invokeInitMethods(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(Init.class)) {
                method.setAccessible(true);

                try {
                    method.invoke(instance);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Ошибка выполнения @Init метода: " +
                                    method.getName() + " в классе " + clazz.getName(), e
                    );
                }
            }
        }
    }
}

