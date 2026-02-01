package it.alessandrobasi.keepassmc.client.config.category;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CategoryAnnotation {
    CategorySection categoria() default CategorySection.Not_sorted;
}
