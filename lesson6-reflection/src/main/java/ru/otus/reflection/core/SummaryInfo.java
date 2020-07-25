package ru.otus.reflection.core;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс, содержащий информацию о результатах тестирования
 */
public class SummaryInfo {
    private static final SummaryInfo EMPTY_SUMMARY = new SummaryInfo();
    private final int totalTests;
    private final int failedTests;
    private final int successTests;
    private final Collection<DetailTestInfo> detailsCollection;

    private SummaryInfo() {
        this.totalTests = 0;
        this.failedTests = 0;
        this.successTests = 0;
        this.detailsCollection = Collections.emptyList();
    }

    private SummaryInfo(int totalTests, int failedTests, int successTests, Collection<DetailTestInfo> detailsCollection) {
        this.totalTests = totalTests;
        this.failedTests = failedTests;
        this.successTests = successTests;
        this.detailsCollection = detailsCollection;
    }

    /**
     * Создание пустого результата тестирования
     *
     * @return пустой результат
     */
    public static SummaryInfo empty() {
        return EMPTY_SUMMARY;
    }

    /**
     * Создание результата тестирования на основе результатов выполнения каждого теста
     *
     * @param details результаты выполнения тестов
     * @return общий результат
     */
    public static SummaryInfo fromDetails(Collection<DetailTestInfo> details) {
        final int size = details.size();
        final int success = (int) details.stream().filter(DetailTestInfo::isSuccess).count();
        return new SummaryInfo(size, size - success, success, details);
    }

    /**
     * @return кол-во успешных тестов
     */
    public int successTests() {
        return successTests;
    }

    /**
     * @return кол-во проваленных тестов
     */
    public int failedTests() {
        return failedTests;
    }

    /**
     * @return общее кол-во тестов
     */
    public int totalTests() {
        return totalTests;
    }

    /**
     * @return результаты выполнения каждого теста в отдельности
     */
    public Collection<DetailTestInfo> getDetails() {
        return Collections.unmodifiableCollection(detailsCollection);
    }
}
