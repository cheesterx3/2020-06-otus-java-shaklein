package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.AtmValidator;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

public class AtmValidatorImpl implements AtmValidator {

    private static String billsDescription(Collection<BillCash> collection) {
        return collection.stream()
                .map(BillCash::nominal)
                .map(String::valueOf)
                .collect(joining(","));
    }

    @Override
    public void checkForBillIsLegal(BillCash billCash) {
        checkForBillIsLegal(billCash, "Bill must not be null");
    }

    @Override
    public void checkForBillsAreLegal(Iterable<BillCash> billCashes) {
        if (isNull(billCashes))
            throw new IllegalArgumentException("Bill collection cannot be null");
        billCashes.forEach(billCash -> checkForBillIsLegal(billCash, "Cash shouldn't contain null"));
    }

    private void checkForBillIsLegal(BillCash cash, String errorMessage) {
        if (isNull(cash))
            throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public void checkForBillsAreSupported(Iterable<BillCash> cash, Predicate<BillCash> billSupport) {
        final Set<BillCash> unsupportedBills = getUnsupportedBillTypes(cash, billSupport);
        if (!unsupportedBills.isEmpty())
            throw new AtmException(String.format("Atm doesn't support nominal [%s]", billsDescription(unsupportedBills)));
    }

    private Set<BillCash> getUnsupportedBillTypes(Iterable<BillCash> cash, Predicate<BillCash> billSupport) {
        final Set<BillCash> unsupportedSet = new HashSet<>();
        cash.forEach(billCash -> {
            if (!billSupport.test(billCash))
                unsupportedSet.add(billCash);
        });
        return unsupportedSet;
    }

    @Override
    public void checkForBillIsSupported(BillCash billCash, Predicate<BillCash> billSupport) {
        if (!billSupport.test(billCash))
            throw new AtmException(String.format("Atm doesn't support nominal %d", billCash.nominal()));
    }

    @Override
    public void checkSumIsAvailable(int amount, IntSupplier balanceSupplier) {
        if (amount <= 0)
            throw new IllegalArgumentException("Nominal should be more than 0");
        if (balanceSupplier.getAsInt() < amount)
            throw new AtmException(String.format("Atm doesn't have enough money to retrieve %d", amount));
    }

}
