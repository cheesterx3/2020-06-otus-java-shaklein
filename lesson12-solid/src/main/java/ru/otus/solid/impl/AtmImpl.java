package ru.otus.solid.impl;

import ru.otus.solid.atm.*;
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
    public Collection<BillCash> putAndReturnUnprocessed(Iterable<BillCash> cash, CashPutStrategy strategy) {
        Objects.requireNonNull(strategy, "Strategy  cannot be null");
        return atmStorage.putAll(cash, strategy);
    }

    @Override
    public void put(BillCash cash) {
        atmStorage.put(cash);
    }

    @Override
    public Collection<BillCash> getCash(int amount, CashRetrieveStrategy strategy) {
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        return atmStorage.get(amount, strategy);
    }

    @Override
    public int balance() {
        return atmStorage.balance();
    }
}
