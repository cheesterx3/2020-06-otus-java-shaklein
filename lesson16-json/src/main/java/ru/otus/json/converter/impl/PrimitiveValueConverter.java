package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;

import static java.util.Objects.nonNull;

public class PrimitiveValueConverter implements Converter {

    @Override
    public String convert(Object fieldValue) {
        return nonNull(fieldValue) ? getValue(fieldValue) : "null";
    }

    private String getValue(Object fieldValue) {
        if (fieldValue.getClass().equals(Character.class))
            return String.format("\"%s\"", fieldValue);
        return fieldValue.toString();
    }
}
