package ru.otus.bytecodes.objects;

import ru.otus.bytecodes.annotation.*;

public class TestContainerImpl implements TestContainer {
    @Override
    @Log
    public void calculate() {

    }

    @Override
    @Log
    public void calculate(int param1) {

    }

    @Override
    @AroundFirst
    @AroundSecond
    @AroundThird
    public int calculate(int param1, int param2) {
        System.out.println("   +++ calculating multiplication of " + param1 + " and " + param2);
        return param1 * param2;
    }

    @Override
    @NPECheck
    public void calculate(int param1, int[] param) {
        throw new NullPointerException("Something wrong. Panic!!!");
    }

    @Override
    @Log
    public void calculate(int param1, String param2, byte param3) {

    }

    @Override
    @NonNullArgs
    public void calculate(int param1, byte param3, String param2) {

    }

    @Override
    @Log
    @NonNullArgs
    @LogAfter
    public void otherMethod(String data) {

    }

    @Override
    @AllPoints
    public int sum(int param1, int param2) {
        return param1 + param2;
    }

}
