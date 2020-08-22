package ru.otus.solid.types;

import ru.otus.solid.atm.CashRetrieveStrategy;
import ru.otus.solid.atm.CashRetrieveStrategyFactory;
import ru.otus.solid.atm.impl.MinBillsCashRetrieveStrategyImpl;

public enum CashRetrieveStrategyType implements CashRetrieveStrategyFactory {
    MIN_BILLS_QUANTITY(new MinBillsCashRetrieveStrategyImpl());

    private final CashRetrieveStrategy strategy;

    CashRetrieveStrategyType(CashRetrieveStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public CashRetrieveStrategy createStrategy() {
        return this.strategy;
    }
}
