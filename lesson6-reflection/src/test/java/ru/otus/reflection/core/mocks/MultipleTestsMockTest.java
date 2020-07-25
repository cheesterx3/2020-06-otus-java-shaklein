package ru.otus.reflection.core.mocks;

import ru.otus.reflection.core.annotations.After;
import ru.otus.reflection.core.annotations.Before;
import ru.otus.reflection.core.annotations.Test;

public class MultipleTestsMockTest {
    @Before
    public void beforeTest() {
    }

    @Test
    public void testMethodFirst() {

    }

    @Test
    public void testMethodSecond() {
        throw new AssertionError("Test is failed");
    }

    @Test
    public void testMethodThird() {

    }

    @After
    public void afterTest() {
    }
}
