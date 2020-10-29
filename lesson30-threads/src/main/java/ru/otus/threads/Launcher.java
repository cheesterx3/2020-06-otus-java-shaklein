package ru.otus.threads;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class Launcher {

    public static final int MAX_VALUE = 10;
    public static final int LOOP_COUNT = 2;

    public static void main(String[] args) {
        log.info("------------------Wait----------------------");
        new ThreadWaitTester().start();
        log.info("------------------Sync------------------------");
        new ThreadSyncTester().start();

    }

    @Slf4j
    static class ThreadWaitTester {
        private final CountDownLatch loopCountLatch = new CountDownLatch(LOOP_COUNT);
        private final AtomicBoolean stopped = new AtomicBoolean(false);
        private boolean reversed = false;
        private int value = 0;

        public void start() {
            new Thread(this::changeValueAndPrint).start();
            new Thread(this::printValue).start();
            try {
                loopCountLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopped.set(true);
        }

        private synchronized void changeValueAndPrint() {
            int increment = 1;
            try {
                while (!stopped.get()) {
                    while (reversed)
                        wait();
                    if (!stopped.get()) {
                        value += increment;
                        if (value == MAX_VALUE || value + increment == 0) increment *= -1;
                        log.info("Value is {}", value);
                        reversed = true;
                        sleep();
                        notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private synchronized void printValue() {
            try {
                while (!stopped.get()) {
                    while (!reversed)
                        wait();
                    if (!stopped.get()) {
                        log.info("Value is {}", value);
                        if (value == MAX_VALUE) loopCountLatch.countDown();
                        reversed = false;
                        sleep();
                        notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Slf4j
    static class ThreadSyncTester {
        private final CountDownLatch startLatch = new CountDownLatch(1);
        private final CountDownLatch loopCountLatch = new CountDownLatch(LOOP_COUNT);
        private final AtomicBoolean stopped = new AtomicBoolean(false);
        private int value = 0;

        public void start() {
            new Thread(this::changeValueAndPrint).start();
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            new Thread(this::printValue).start();
            try {
                loopCountLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopped.set(true);
        }

        private void changeValueAndPrint() {
            int increment = 1;
            startLatch.countDown();
            while (!stopped.get()) {
                synchronized (this) {
                    value += increment;
                    if (value == MAX_VALUE || value + increment == 0) increment *= -1;
                    log.info("Value is {}", value);
                }
                sleep();
            }
        }

        private void printValue() {
            while (!stopped.get()) {
                synchronized (this) {
                    log.info("Value is {}", value);
                    if (value == MAX_VALUE) loopCountLatch.countDown();
                }
                sleep();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
