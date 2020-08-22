package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.CashPutStrategy;
import ru.otus.solid.types.BillCash;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Реализация стратегии зачисления "всё или ничего"
 */
public class AllOrNothingCashPutStrategyImpl implements CashPutStrategy {

    @Override
    public Collection<BillCash> calcProcessable(Iterable<BillCash> billCashes, ToIntFunction<BillCash> spaceRequester) {
        final List<BillCash> overheadCashList = getOverheadCashTypes(billCashes, spaceRequester);
        if (overheadCashList.isEmpty()) {
            return StreamSupport.stream(billCashes.spliterator(), false)
                    .collect(toList());
        }
        return Collections.emptyList();
    }

    private List<BillCash> getOverheadCashTypes(Iterable<BillCash> cash, ToIntFunction<BillCash> spaceRequester) {
        final EnumMap<BillCash, Integer> temporary = new EnumMap<>(BillCash.class);
        cash.forEach(billCash -> temporary.merge(billCash, 1, Integer::sum));
        return temporary.entrySet().stream()
                .filter(entry -> spaceRequester.applyAsInt(entry.getKey()) < entry.getValue())
                .map(Map.Entry::getKey)
                .collect(toList());
    }
}
