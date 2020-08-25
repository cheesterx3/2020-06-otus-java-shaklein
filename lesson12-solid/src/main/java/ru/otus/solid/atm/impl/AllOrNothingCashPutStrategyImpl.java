package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.CashPutStrategy;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.function.ToIntFunction;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Реализация стратегии зачисления "всё или ничего"
 */
public class AllOrNothingCashPutStrategyImpl implements CashPutStrategy {

    @Override
    public Collection<BillCash> calcProcessable(Iterable<BillCash> billCashes, ToIntFunction<BillCash> spaceRequester) {
        if (hasOverheadCash(billCashes, spaceRequester))
            return Collections.emptyList();

        return StreamSupport.stream(billCashes.spliterator(), false)
                .collect(toList());
    }

    private boolean hasOverheadCash(Iterable<BillCash> cash, ToIntFunction<BillCash> spaceRequester) {
        final EnumMap<BillCash, Integer> temporary = new EnumMap<>(BillCash.class);
        cash.forEach(billCash -> temporary.merge(billCash, 1, Integer::sum));
        return temporary.entrySet().stream()
                .anyMatch(entry -> spaceRequester.applyAsInt(entry.getKey()) < entry.getValue());
    }
}
