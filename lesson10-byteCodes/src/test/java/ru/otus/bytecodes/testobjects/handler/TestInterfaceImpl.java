package ru.otus.bytecodes.testobjects.handler;

public class TestInterfaceImpl implements TestInterface {
    @Override
    public int calculate() {
        return 0;
    }

    @Override
    public int calculate(int param1, int param2) {
        return param1 + param2;
    }

    @Override
    public String calculate(int param1, String param2) {
        return param2 + ": " + param1;
    }

    @Override
    public void calculate(long param1, double param2, String param3) {
        System.out.println("Calculating complex");
    }
}
