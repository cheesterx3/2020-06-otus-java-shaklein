package ru.otus.solid.atm.impl;

import ru.otus.solid.atm.*;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.*;
import java.util.function.Predicate;


public class CellAtmStorageImpl implements AtmStorage {
    private final AtmValidator atmValidator;
    private final Map<BillCash, List<Cell>> storage = new EnumMap<>(BillCash.class);

    public CellAtmStorageImpl(Collection<BillCell> cells, AtmValidator atmValidator) {
        Objects.requireNonNull(cells, "Cells shouldn't be null");
        this.atmValidator = Objects.requireNonNull(atmValidator, "Validator shouldn't be null");
        cells.forEach(cell -> storage.computeIfAbsent(cell.cashType(), billCash -> new ArrayList<>()).add(cell));
    }

    @Override
    public void put(BillCash billCash) {
        atmValidator.checkForBillIsLegal(billCash);
        atmValidator.checkForBillIsSupported(billCash, storage::containsKey);
        doPut(billCash);
    }

    @Override
    public Collection<BillCash> putAll(Iterable<BillCash> billCashes, CashPutStrategy strategy) {
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        atmValidator.checkForBillsAreLegal(billCashes);
        atmValidator.checkForBillsAreSupported(billCashes, storage::containsKey);
        final Collection<BillCash> processableCashes = strategy.calcProcessable(billCashes, this::availableSpace);
        processableCashes.forEach(this::doPut);
        return processableCashes;
    }

    private void doPut(BillCash cash) {
        storage.get(cash).stream()
                .filter(Cell::hasSpace)
                .findAny()
                .orElseThrow(() -> new AtmException(String.format("Unable to put cash with nominal %d. Cell is full", cash.nominal())))
                .put();
    }

    @Override
    public int availableSpace(BillCash billCash) {
        Objects.requireNonNull(billCash, "Bill cannot be null");
        return storage.get(billCash)
                .stream()
                .map(Cell::freeSpace)
                .reduce(0, Integer::sum);
    }

    @Override
    public Collection<BillCash> get(int amount, CashRetrieveStrategy strategy) {
        Objects.requireNonNull(strategy, "Strategy cannot be null");
        atmValidator.checkSumIsAvailable(amount, this::balance);
        final Collection<BillCash> billsToRetrieve = strategy.retrieve(amount, this::availableCash);
        billsToRetrieve.forEach(billCash -> storage.get(billCash)
                .stream()
                .filter(Predicate.not(Cell::isEmpty))
                .findAny()
                .ifPresent(Cell::get));
        return billsToRetrieve;
    }

    @Override
    public int availableCash(BillCash billCash) {
        Objects.requireNonNull(billCash, "Bill cannot be null");
        return storage.get(billCash)
                .stream()
                .map(Cell::amount)
                .reduce(0, Integer::sum);
    }

    @Override
    public int balance() {
        return storage.entrySet().stream()
                .map(entry -> entry.getKey()
                        .total(entry.getValue().stream()
                                .map(Cell::amount)
                                .reduce(0, Integer::sum)))
                .reduce(0, Integer::sum);

    }
}
