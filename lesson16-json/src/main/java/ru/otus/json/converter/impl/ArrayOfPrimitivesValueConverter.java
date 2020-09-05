package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ArrayOfPrimitivesValueConverter implements Converter {

    @Override
    public String convert(Object fieldValue) {
        if (nonNull(fieldValue)) {
            String className = fieldValue.getClass().getName();
            int arrayDimension = className.lastIndexOf("[") + 1;
            return arrayToString(arrayDimension, fieldValue);
        }
        return "null";
    }

    private String arrayToString(int dimension, Object arrayValue) {
        final StringBuilder builder = new StringBuilder();
        final List<Object> list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(arrayValue); i++)
            if (dimension > 1)
                list.add(arrayToString(dimension - 1, Array.get(arrayValue, i)));
            else list.add(Array.get(arrayValue, i));
        builder.append(String.format("[%s]",
                list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","))));
        return builder.toString();
    }
}
