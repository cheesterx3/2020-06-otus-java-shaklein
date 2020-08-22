package ru.otus.solid.types;

import ru.otus.solid.atm.Cash;

public enum BillCash implements Cash {
    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    TWO_HUNDRED(500),
    ONE_HUNDRED(100);

    private final int nominal;

    BillCash(int nominal) {
        this.nominal = nominal;
    }

    @Override
    public int nominal() {
        return nominal;
    }

    @Override
    public String toString() {
        return "BillCash{" +
                "nominal=" + nominal +
                '}';
    }
}
