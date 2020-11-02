package ru.otus.threads;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

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
            val thread = new Thread(() -> changeValueAndPrint(true));
            thread.start();
            val thread2 = new Thread(() -> changeValueAndPrint(false));
            thread2.start();
            try {
                loopCountLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopped.set(true);
            thread.interrupt();
            thread2.interrupt();
        }

        private synchronized void changeValueAndPrint(boolean shouldChange) {
            int increment = shouldChange ? 1 : 0;
            try {
                while (!stopped.get()) {
                    while (reversed == shouldChange) {
                        wait();
                    }
                    if (!stopped.get()) {
                        value += increment;
                        if (value == MAX_VALUE || value + increment == 0) increment *= -1;
                        if (value == MAX_VALUE && !shouldChange) loopCountLatch.countDown();
                        log.info("Value is {}", value);
                        reversed = shouldChange;
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
            val thread = new Thread(() -> changeValueAndPrint(true));
            thread.start();
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            val thread2 = new Thread(() -> changeValueAndPrint(false));
            thread2.start();
            try {
                loopCountLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopped.set(true);
            thread.interrupt();
            thread2.interrupt();
        }

        private void changeValueAndPrint(boolean shouldChange) {
            int increment = shouldChange ? 1 : 0;
            startLatch.countDown();
            while (!stopped.get()) {
                synchronized (this) {
                    value += increment;
                    if (value == MAX_VALUE || value + increment == 0) increment *= -1;
                    if (value == MAX_VALUE && !shouldChange) loopCountLatch.countDown();
                    log.info("Value is {}", value);
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
