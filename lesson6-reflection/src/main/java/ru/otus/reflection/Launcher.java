package ru.otus.reflection;

import ru.otus.reflection.core.DetailTestInfo;
import ru.otus.reflection.core.impl.TestLauncherImpl;

public class Launcher {
    public static void main(String[] args) {
        final var testLauncher = new TestLauncherImpl();
        final var summaryInfo = testLauncher.executeTest(TestingClass.class);
        System.out.println(String.format("Total executed tests: %d", summaryInfo.totalTests()));
        System.out.println(String.format("Total failed tests: %d", summaryInfo.failedTests()));
        System.out.println(String.format("Total success tests: %d", summaryInfo.successTests()));

        summaryInfo.getDetails().forEach(Launcher::printDetail);
    }

    private static void printDetail(DetailTestInfo detailTestInfo) {
        System.out.println(String.format("%s: %s", detailTestInfo.getName(), testResultInfo(detailTestInfo)));
        if (!detailTestInfo.isSuccess()) {
            detailTestInfo.getThrowable().ifPresent(throwable ->
                    System.out.println(String.format("\t %s", throwable.getMessage())));

        }
    }

    private static String testResultInfo(DetailTestInfo detailTestInfo) {
        return detailTestInfo.isSuccess()
                ? Color.GREEN.wrap("success")
                : Color.RED.wrap("failed");
    }

    private enum Color {
        RESET("\033[0m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        WHITE("\033[0;37m");
        private final String code;

        Color(String code) {
            this.code = code;
        }

        String wrap(String string) {
            return String.format("%s%s%s", code, string, RESET.code);
        }
    }
}
