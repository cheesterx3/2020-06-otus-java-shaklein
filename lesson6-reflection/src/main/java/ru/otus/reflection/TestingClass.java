package ru.otus.reflection;

import ru.otus.reflection.core.annotations.After;
import ru.otus.reflection.core.annotations.Before;
import ru.otus.reflection.core.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestingClass {
    public static final int EXPECTED_SIZE = 5;
    public static final int EXPECTED_INCORRECT_SIZE = 6;
    private List<String> items;

    @Before
    private void setUp() {
        items = IntStream.range(0, 5).mapToObj(value -> "test_" + value).collect(Collectors.toList());
    }

    @Test
    private void shouldCountCorrectSize() {
        if (items.size() != EXPECTED_SIZE)
            throw new RuntimeException(String.format("incorrect list size. Expected %d but was %d", EXPECTED_SIZE, items.size()));
    }

    @Test
    private void shouldFailOnIncorrectCountSize() {
        if (items.size() != EXPECTED_INCORRECT_SIZE)
            throw new RuntimeException(String.format("incorrect list size. Expected %d but was %d", EXPECTED_INCORRECT_SIZE, items.size()));
    }

    @Test
    private void shouldRemoveFromList() {
        items.remove(0);
        if (items.size() != EXPECTED_SIZE-1)
            throw new RuntimeException(String.format("incorrect list size. Expected %d but was %d", EXPECTED_SIZE-1, items.size()));
    }

    @After
    private void doAfter() {
        items.clear();
    }
}
