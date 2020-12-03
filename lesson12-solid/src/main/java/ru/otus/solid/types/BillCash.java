package ru.otus.solid.types;

public enum BillCash {
    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    TWO_HUNDRED(200),
    ONE_HUNDRED(100);

    private final int nominal;

    BillCash(int nominal) {
        this.nominal = nominal;
    }

    public int nominal() {
        return nominal;
    }

    public int total(int cashCount) {
        return nominal() * cashCount;
    }

    @Override
    public String toString() {
        return "BillCash{" +
                "nominal=" + nominal +
                '}';
    }
}
