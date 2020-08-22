package ru.otus.solid.impl;

import ru.otus.solid.atm.Atm;
import ru.otus.solid.atm.AtmStorage;
import ru.otus.solid.atm.CashPutStrategyFactory;
import ru.otus.solid.atm.CashRetrieveStrategyFactory;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class AtmImpl implements Atm {
    private final AtmStorage atmStorage;

    public AtmImpl(AtmStorage atmStorage) {
        this.atmStorage = requireNonNull(atmStorage, "Storage cannot be null");
    }

    @Override
    public Collection<BillCash> putAndReturnUnprocessed(Iterable<BillCash> cash, CashPutStrategyFactory strategyFactory) {
        Objects.requireNonNull(strategyFactory, "Strategy factory cannot be null");
        return atmStorage.putAll(cash, strategyFactory.createStrategy());
    }

    @Override
    public void put(BillCash cash) {
        atmStorage.put(cash);
    }

    @Override
    public Collection<BillCash> getCash(int amount, CashRetrieveStrategyFactory strategyFactory) {
        Objects.requireNonNull(strategyFactory, "Strategy factory cannot be null");
        return atmStorage.get(amount, strategyFactory.createStrategy());
    }

    @Override
    public int balance() {
        return atmStorage.balance();
    }
}
