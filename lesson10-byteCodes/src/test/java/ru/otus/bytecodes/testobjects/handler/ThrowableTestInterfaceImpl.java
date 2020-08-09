package ru.otus.bytecodes.testobjects.handler;

public class ThrowableTestInterfaceImpl implements TestInterface {
    @Override
    public int calculate() {
        throw new RuntimeException();
    }

    @Override
    public int calculate(int param1, int param2) {
        throw new RuntimeException();
    }

    @Override
    public String calculate(int param1, String param2) {
        throw new RuntimeException();
    }

    @Override
    public void calculate(long param1, double param2, String param3) {
        throw new RuntimeException();
    }
}
