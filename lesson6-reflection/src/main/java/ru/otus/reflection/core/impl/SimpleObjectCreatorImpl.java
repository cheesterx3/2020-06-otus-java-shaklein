package ru.otus.reflection.core.impl;

import ru.otus.reflection.core.ObjectCreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class SimpleObjectCreatorImpl implements ObjectCreator {
    @Override
    public Optional<Object> createObject(Class<?> objectClass) {
        try {
            final Object newInstance = objectClass.getDeclaredConstructor().newInstance();
            return Optional.of(newInstance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
