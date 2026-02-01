package it.alessandrobasi.keepassmc.client.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationSearch {

    public static Set<Class<?>> search(Class<? extends Annotation> tClass, String classPath) {
        Set<Class<?>> result;
        result = new Reflections(classPath).getTypesAnnotatedWith(tClass);
        return result;
    }

}
