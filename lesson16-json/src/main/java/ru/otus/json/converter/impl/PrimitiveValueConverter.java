package ru.otus.json.converter.impl;

import ru.otus.json.converter.Converter;

import static java.util.Objects.nonNull;

/**
 * Конвертер значений всех примитивных типов.
 *
 * @implNote По сути для всех примитивных значений достаточно использовать toString для решения задачи.
 * Поэтому нет смысла создавать по каждому конвертеру на тип на данный момент.
 */
class PrimitiveValueConverter implements Converter {

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
