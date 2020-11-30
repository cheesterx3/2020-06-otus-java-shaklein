package ru.otus.gc;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Benchmark {
    private final int loopCounter;
    private final int size;
    private final boolean withLeak;

    public Benchmark(int loopCounter, int size, boolean withLeak) {
        this.loopCounter = loopCounter;
        this.size = size;
        this.withLeak = withLeak;
    }

    void run() {
        final List<Object> list = new ArrayList<>();
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new Random(loopCounter).nextInt();
                if (withLeak && i % 3 == 0)
                    list.add(new Random(loopCounter).nextInt());
            }
        }
    }

}
