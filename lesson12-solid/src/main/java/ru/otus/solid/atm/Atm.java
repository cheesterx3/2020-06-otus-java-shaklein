package ru.otus.solid.atm;

import ru.otus.solid.atm.CashPutStrategyFactory;
import ru.otus.solid.atm.CashRetrieveStrategyFactory;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.Collection;

/**
 * Интерфейс банкомата
 */
public interface Atm {
    /**
     * Положить средства согласно выбранной стратегии
     *
     * @param cash            набор средств
     * @param strategyFactory фабрика стратегий
     * @return список средств корректно зачисленных в банкомат
     * @throws IllegalArgumentException в случае если в наборе купюр присутствует null-значение
     * @throws AtmException             в случае если в наборе присутствую купюры, не поддерживаемые хранилищем
     * @throws NullPointerException     в случае если стратегия равна null
     */
    Collection<BillCash> putAndReturnUnprocessed(Iterable<BillCash> cash, CashPutStrategyFactory strategyFactory);

    /**
     * Снять средства согласно выбранной стратегии
     *
     * @param amount          объём средств
     * @param strategyFactory фабрика стратегий
     * @return список купюр
     * @throws IllegalArgumentException в случае запроса суммы меньше или равной нулю
     * @throws AtmException             в случае если запрашиваемая сумма больше, чем есть в хранилище либо
     *                                  в случае нехватки купюр определённого номинала для выдачи полной суммы
     * @throws NullPointerException     в случае если стратегия равна null
     */
    Collection<BillCash> getCash(int amount, CashRetrieveStrategyFactory strategyFactory);

    /**
     * @return текущий баланс банкомата
     */
    int balance();
}
