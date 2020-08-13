package ru.otus.bytecodes.testobjects.handler;

public interface TestInterface {
    int calculate();

    int calculate(int param1, int param2);

    String calculate(int param1, String param2);

    void calculate(long param1, double param2, String param3);
}
