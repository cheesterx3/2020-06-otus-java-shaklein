package ru.otus.reflection.core.mocks;

import ru.otus.reflection.core.annotations.After;
import ru.otus.reflection.core.annotations.Before;
import ru.otus.reflection.core.annotations.Test;

public class MockWithoutDefaultConstructorTest {

    public MockWithoutDefaultConstructorTest(String someParam) {
    }

    @Before
    public void beforeTest() {

    }

    @Test
    public void testMethod() {

    }

    @After
    public void afterTest() {

    }
}
