package ru.otus.solid.atm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.solid.atm.AtmStorage;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MinBillsCashRetrieveStrategyImplTest {
    public static final int BALANCE = 18700;
    private MinBillsCashRetrieveStrategyImpl strategy;
    private AtmStorage storage;

    @BeforeEach
    void setUp() {
        strategy = new MinBillsCashRetrieveStrategyImpl();
        storage = mock(AtmStorage.class);
        when(storage.balance()).thenReturn(BALANCE);
        when(storage.availableCash(eq(BillCash.FIVE_THOUSAND))).thenReturn(2);
        when(storage.availableCash(eq(BillCash.TWO_THOUSAND))).thenReturn(3);
        when(storage.availableCash(eq(BillCash.FIVE_HUNDRED))).thenReturn(5);
        when(storage.availableCash(eq(BillCash.TWO_HUNDRED))).thenReturn(1);
    }

    @Test
    void shouldRetrieveCorrectCountOfBills() {
        Collection<BillCash> cashes = strategy.retrieve(8000, storage::availableCash);
        assertThat(cashes)
                .containsExactly(BillCash.FIVE_THOUSAND,
                        BillCash.TWO_THOUSAND,
                        BillCash.FIVE_HUNDRED,
                        BillCash.FIVE_HUNDRED);
    }

    @Test
    void shouldThrowAnExceptionIfRequestedAmountDoesntMultiplyMinBill() {
        assertThatThrownBy(() -> strategy.retrieve(8100, storage::availableCash))
                .isInstanceOf(AtmException.class);
    }

    @Test
    void shouldThrowAnExceptionIfRequestedAmountIsNotAvailable() {
        assertThatThrownBy(() -> strategy.retrieve(8400, storage::availableCash))
                .isInstanceOf(AtmException.class);
    }


}