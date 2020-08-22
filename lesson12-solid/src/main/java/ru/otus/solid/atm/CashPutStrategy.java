package ru.otus.solid.atm;

import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.function.ToIntFunction;

/**
 * Интерфейс стратегии зачисления
 */
public interface CashPutStrategy {
    /**
     * Получение списка доступных для зачисления купюр
     *
     * @param billCashes     набор купюр
     * @param spaceRequester фкнция запроса на наличие доступного места для типа купюры
     * @return список купюр доступных для зачисления
     */
    Collection<BillCash> calcProcessable(Iterable<BillCash> billCashes, ToIntFunction<BillCash> spaceRequester);
}
