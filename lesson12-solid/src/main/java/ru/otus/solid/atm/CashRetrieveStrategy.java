package ru.otus.solid.atm;

import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.function.ToIntFunction;

/**
 * Интерфейс стратегии снятия
 */
public interface CashRetrieveStrategy {
    /**
     * Получение списка купюр доступных для снятия
     *
     * @param amount          объём средств необходимый для снятия
     * @param amountRequester функция запроса доступного кол-ва купюр определённого вида
     * @return список купюр, которые можно снять
     * @throws AtmException в случае отсутствия средств или купюр для снятия корректной суммы
     */
    Collection<BillCash> retrieve(int amount, ToIntFunction<BillCash> amountRequester);
}
