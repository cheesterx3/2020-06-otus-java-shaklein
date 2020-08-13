package ru.otus.bytecodes.objects;

public interface TestContainer {
    void calculate();

    void calculate(int param1);

    int calculate(int param1, int param2);

    void calculate(int param1, int[] param);

    void calculate(int param1, String param2, byte param3);

    void calculate(int param1, byte param3, String param2);

    void otherMethod(String data);

    int sum(int param1,int param2);
}
