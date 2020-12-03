package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.CashRetrieveStrategy;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация стратегии снятия по принципу минимального кол-ва купюр
 */
public class MinBillsCashRetrieveStrategyImpl implements CashRetrieveStrategy {

    @Override
    public Collection<BillCash> retrieve(int amount, ToIntFunction<BillCash> amountRequester) {
        final List<BillCash> billCashes = Stream.of(BillCash.values())
                .filter(billCash -> amountRequester.applyAsInt(billCash) > 0)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        if (!billCashes.isEmpty()) {
            checkMinBill(amount, billCashes);
            final List<BillCash> result = new ArrayList<>();
            for (BillCash billCash : billCashes) {
                if (amount >= billCash.nominal()) {
                    int quantity = Math.min(amount / billCash.nominal(), amountRequester.applyAsInt(billCash));
                    result.addAll(Collections.nCopies(quantity, billCash));
                    amount -= billCash.total(quantity);
                    if (amount == 0)
                        return result;
                }
            }
        }
        throw new AtmException(String.format("Not enough cash in atm. Requested %d of cash is not available", amount));
    }

    private void checkMinBill(int amount, Collection<BillCash> billCashes) {
        final Optional<Integer> min = billCashes.stream()
                .map(BillCash::nominal)
                .min(Comparator.naturalOrder());
        if (min.map(integer -> (amount % integer) > 0)
                .filter(Boolean::booleanValue)
                .isPresent())
            throw new AtmException(String.format("Incorrect amount (%d) is requested. ", amount));
    }
}
