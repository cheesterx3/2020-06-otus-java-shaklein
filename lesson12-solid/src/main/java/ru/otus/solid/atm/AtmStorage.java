package ru.otus.solid.atm;

import ru.otus.solid.types.BillCash;

import java.util.Collection;

/**
 * Интерфейс хранилища купюр банкомата
 */
public interface AtmStorage {
    /**
     * Пополнение соответствующей купюрой
     *
     * @param cash тип купюры для пополнения
     * @throws IllegalArgumentException              в случае передачи null в аргументах
     * @throws ru.otus.solid.exceptions.AtmException в случае если хранилище не поддерживает данный тип купюр
     */
    void put(BillCash cash);

    /**
     * Пополнение набором купюр
     *
     * @param cash     набор купюр
     * @param strategy стратегия зачисления
     * @return список купюр, удачно положенных в хранилище
     * @throws IllegalArgumentException              в случае если в наборе купюр присутствует null-значение
     * @throws ru.otus.solid.exceptions.AtmException в случае если в наборе присутствую купюры, не поддерживаемые хранилищем
     * @throws NullPointerException                  в случае если стратегия равна null
     */
    Collection<BillCash> putAll(Iterable<BillCash> cash, CashPutStrategy strategy);

    /**
     * Получение купюр из хранилища на переданную сумму
     *
     * @param amount   объём средств для снятия
     * @param strategy стратегия снятия
     * @return список купюр
     * @throws IllegalArgumentException              в случае запроса суммы меньше или равной нулю
     * @throws ru.otus.solid.exceptions.AtmException в случае если запрашиваемая сумма больше, чем есть в хранилище либо
     *                                               в случае нехватки купюр определённого номинала для выдачи полной суммы
     * @throws NullPointerException                  в случае если стратегия равна null
     */
    Collection<BillCash> get(int amount, CashRetrieveStrategy strategy);

    /**
     * Получение кол-ва купюр переданного типа
     *
     * @param billCash тип купюр
     * @return кол-во доступных купюр
     * @throws NullPointerException если передан null в аргументе
     */
    int availableCash(BillCash billCash);

    /**
     * Получение доступного места для переданного типа курпюр
     *
     * @param billCash тип купюр
     * @return объём свободного места
     * @throws NullPointerException если передан null в аргументе
     */
    int availableSpace(BillCash billCash);

    /**
     * @return общий баланс хранилища
     */
    int balance();
}
