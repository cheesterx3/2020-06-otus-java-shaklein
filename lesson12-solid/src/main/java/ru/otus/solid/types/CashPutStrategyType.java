package ru.otus.solid.types;

import ru.otus.solid.atm.CashPutStrategy;
import ru.otus.solid.atm.impl.AllOrNothingCashPutStrategyImpl;

import java.util.Collection;
import java.util.function.ToIntFunction;

public enum CashPutStrategyType implements CashPutStrategy {
    ALL_OR_NOTHING(new AllOrNothingCashPutStrategyImpl());

    private final CashPutStrategy strategy;

    CashPutStrategyType(CashPutStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Collection<BillCash> calcProcessable(Iterable<BillCash> billCashes, ToIntFunction<BillCash> spaceRequester) {
        return strategy.calcProcessable(billCashes, spaceRequester);
    }
}
