package ru.otus.solid.types;

import ru.otus.solid.atm.CashPutStrategy;
import ru.otus.solid.atm.CashPutStrategyFactory;
import ru.otus.solid.atm.impl.AllOrNothingCashPutStrategyImpl;

public enum CashPutStrategyType implements CashPutStrategyFactory {
    ALL_OR_NOTHING(new AllOrNothingCashPutStrategyImpl());

    private final CashPutStrategy strategy;

    CashPutStrategyType(CashPutStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public CashPutStrategy createStrategy() {
        return this.strategy;
    }
}
