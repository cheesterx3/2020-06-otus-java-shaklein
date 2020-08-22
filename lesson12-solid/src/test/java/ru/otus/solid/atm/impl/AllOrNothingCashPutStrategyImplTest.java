package ru.otus.solid.atm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AllOrNothingCashPutStrategyImplTest {
    private AllOrNothingCashPutStrategyImpl strategy;

    @BeforeEach
    void setUp() {
        strategy = new AllOrNothingCashPutStrategyImpl();
    }

    @Test
    void shouldReturnSourceCollectionIfAllIsAvailableToPut() {
        List<BillCash> billsToCheck = List.of(BillCash.FIVE_HUNDRED, BillCash.FIVE_HUNDRED, BillCash.FIVE_HUNDRED);
        Collection<BillCash> cashes = strategy.calcProcessable(billsToCheck, bill -> 3);
        assertThat(cashes).isEqualTo(billsToCheck);
    }

    @Test
    void shouldReturnEmptyCollectionIfAnyOfBillIsNotAvailableToPut() {
        List<BillCash> billsToCheck = List.of(BillCash.FIVE_HUNDRED,
                BillCash.FIVE_HUNDRED,
                BillCash.ONE_THOUSAND,
                BillCash.ONE_THOUSAND,
                BillCash.ONE_THOUSAND);
        Collection<BillCash> cashes = strategy.calcProcessable(billsToCheck, bill -> 2);
        assertThat(cashes).isEmpty();
    }
}