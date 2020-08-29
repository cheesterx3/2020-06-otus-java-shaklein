package ru.otus.solid.atm;

import ru.otus.solid.types.BillCash;

import java.util.function.Predicate;

/**
 * Интерфейс валидации данных. В идеале его бы перенести в аспекты и задействовать АОП, но за усложнение решения только минусуют:)
 */
public interface AtmValidator {
    /**
     * Проверка купюры на корректность
     *
     * @param billCash купюра
     * @throws IllegalArgumentException если в аргументе null
     */
    void checkForBillIsLegal(BillCash billCash);

    /**
     * Проверка купюр на корректность
     *
     * @param billCashes набор купюр
     * @throws IllegalArgumentException если в  списке присутствует null
     */
    void checkForBillsAreLegal(Iterable<BillCash> billCashes);

    /**
     * Проверка на доступность всех купюр в наборе
     *
     * @param cash        набор купюр
     * @param billSupport функция проверки на доступность
     * @throws ru.otus.solid.exceptions.AtmException в случе если в наборе присутствует неподдерживаемые купюры
     */
    void checkForBillsAreSupported(Iterable<BillCash> cash, Predicate<BillCash> billSupport);

    /**
     * Проверка на доступность купюры
     *
     * @param cash        купюра
     * @param billSupport функция проверки на доступность
     * @throws ru.otus.solid.exceptions.AtmException в случе если купюра не поддерживается
     */
    void checkForBillIsSupported(BillCash cash, Predicate<BillCash> billSupport);

    /**
     * Проверка на корректность запрашиваемой суммы средств
     *
     * @param amount          объём средств
     * @param balance         текущий баланс
     * @throws IllegalArgumentException              в случае запроса суммы меньше или равной нулю
     * @throws ru.otus.solid.exceptions.AtmException в случае если запрашиваемая сумма больше, чем есть в хранилище либо
     */
    void checkSumIsAvailable(int amount, int balance);
}
