package it.alessandrobasi.keepassmc.client.config.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GroupAnnotation {
    String name() default "";
    String description() default "";
    String tooltip() default "";
    boolean collapsed() default false;
    boolean isRoot() default false;
    int order() default 999;
}
