package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;

import static java.util.Objects.nonNull;

class StringConverter implements Converter {
    @Override
    public String convert(Object object) {
        return nonNull(object) ? String.format("\"%s\"", object.toString()) : "null";
    }
}
