package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.BillCell;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import static java.util.Objects.requireNonNull;

public class BillCellImpl implements BillCell {
    private final BillCash cashType;
    private final int capacity;
    private int amount;

    private BillCellImpl(BillCash cashType, int capacity, int amount) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Cell capacity must be more than 0");
        if (amount < 0)
            throw new IllegalArgumentException("Cell amount must not be negative");
        this.cashType = requireNonNull(cashType, "Cash type cannot be null");
        this.capacity = capacity;
        this.amount = amount;
    }

    public static BillCell withAmount(BillCash cashType, int capacity, int amount) {
        return new BillCellImpl(cashType, capacity, amount);
    }

    public static BillCell empty(BillCash cashType, int capacity) {
        return new BillCellImpl(cashType, capacity, 0);
    }

    @Override
    public BillCash cashType() {
        return cashType;
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public int amount() {
        return amount;
    }

    @Override
    public void put() {
        if (amount == capacity)
            throw new AtmException("Cell is full");
        amount += 1;
    }

    @Override
    public void get() {
        if (amount == 0)
            throw new AtmException("Cell is empty");
        amount -= 1;
    }
}
