package ru.otus.gc;


class Benchmark {
    private final int loopCounter;
    private final int size;

    public Benchmark(int loopCounter, int size) {
        this.loopCounter = loopCounter;
        this.size = size;
    }

    void run() throws InterruptedException {
        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new byte[512];
            }
            //Thread.sleep(10); //Label_1
        }
    }

}
