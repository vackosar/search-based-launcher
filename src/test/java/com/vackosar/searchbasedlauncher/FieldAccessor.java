package com.vackosar.searchbasedlauncher;

import java.lang.reflect.Field;

public class FieldAccessor {
    public static Field getField(Class clazz, String name) {
        final Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            if (name.equals(field.getName())) {
                field.setAccessible(true);
                return field;
            }
        }
        throw new RuntimeException("Not found field name: " + name);
    }

    public static void setField(Object object, String name, Object value) {
        final Field field = getField(object.getClass(), name);
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object object, String name) {
        final Field field = getField(object.getClass(), name);
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
