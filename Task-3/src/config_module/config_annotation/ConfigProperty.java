package config_module.config_annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {

    String configFileName() default "";

    String propertyName() default "";

    Class<?> type() default Object.class;
}
