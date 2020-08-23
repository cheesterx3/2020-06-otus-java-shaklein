package ru.otus.solid.types;

import ru.otus.solid.atm.CashRetrieveStrategy;
import ru.otus.solid.atm.impl.MinBillsCashRetrieveStrategyImpl;

import java.util.Collection;
import java.util.function.ToIntFunction;

public enum CashRetrieveStrategyType implements CashRetrieveStrategy {
    MIN_BILLS_QUANTITY(new MinBillsCashRetrieveStrategyImpl());

    private final CashRetrieveStrategy strategy;

    CashRetrieveStrategyType(CashRetrieveStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Collection<BillCash> retrieve(int amount, ToIntFunction<BillCash> amountRequester) {
        return strategy.retrieve(amount,amountRequester);
    }
}
