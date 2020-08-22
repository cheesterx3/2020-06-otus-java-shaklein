package ru.otus.solid.atm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.solid.atm.AtmValidator;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AtmValidatorImplTest {

    private AtmValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AtmValidatorImpl();
    }

    @Test
    void shouldThrowAnExceptionIfBillCashIsNull() {
        assertThatThrownBy(() -> validator.checkForBillIsLegal(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowAnExceptionIfAnyOfBillInCollectionCashIsNull() {
        List<BillCash> cashes = new ArrayList<>();
        cashes.add(BillCash.FIVE_HUNDRED);
        cashes.add(null);
        cashes.add(BillCash.ONE_HUNDRED);
        assertThatThrownBy(() -> validator.checkForBillsAreLegal(cashes))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowAnExceptionIfAnyOfBillInCollectionIsNotSupported() {
        List<BillCash> cashes = List.of(BillCash.FIVE_HUNDRED, BillCash.ONE_HUNDRED);
        assertThatThrownBy(() ->
                validator.checkForBillsAreSupported(cashes, billCash -> billCash != BillCash.ONE_HUNDRED)
        ).isInstanceOf(AtmException.class);
    }

    @Test
    void shouldThrowAnExceptionIfBillIsNotSupported() {
        assertThatThrownBy(() ->
                validator.checkForBillIsSupported(BillCash.ONE_HUNDRED, billCash -> billCash != BillCash.ONE_HUNDRED)
        ).isInstanceOf(AtmException.class);
    }

    @Test
    void shouldThrowAnExceptionIfRequestedAmountIsIllegalOrLessThanBalance() {
        assertThatThrownBy(() -> validator.checkSumIsAvailable(-1, () -> 10))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> validator.checkSumIsAvailable(15, () -> 10))
                .isInstanceOf(AtmException.class);
    }
}