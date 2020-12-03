package ru.otus.solid.atm.impl;

import org.junit.jupiter.api.Test;
import ru.otus.solid.atm.BillCell;
import ru.otus.solid.exceptions.AtmException;
import ru.otus.solid.types.BillCash;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BillCellImplTest {

    @Test
    void shouldCreateBillCellCorrectly() {
        BillCash cashType = BillCash.FIVE_THOUSAND;
        int capacity = 10;
        int amount = 10;
        BillCell billCell = BillCellImpl.withAmount(cashType, capacity, amount);
        assertThat(billCell.cashType()).isEqualTo(cashType);
        assertThat(billCell.amount()).isEqualTo(amount);
        assertThat(billCell.capacity()).isEqualTo(capacity);
    }

    @Test
    void shouldThrowExceptionOnIncorrectData() {
        assertThatThrownBy(() -> BillCellImpl.withAmount(null, 1, 1))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, 0, 1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, 1, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldIncreaseAmountOnAdd() {
        BillCell cell = BillCellImpl.empty(BillCash.FIVE_THOUSAND, 10);
        cell.put();
        assertThat(cell.amount()).isEqualTo(1);
        cell.put();
        assertThat(cell.amount()).isEqualTo(2);
    }

    @Test
    void shouldDecreaseAmountOnGet() {
        BillCell cell = BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, 10, 10);
        cell.get();
        assertThat(cell.amount()).isEqualTo(9);
        cell.get();
        assertThat(cell.amount()).isEqualTo(8);
    }

    @Test
    void shouldThrowAnExceptionWhenAddingToFullCell() {
        BillCell cell = BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, 10, 10);
        assertThatThrownBy(cell::put).isInstanceOf(AtmException.class);
    }

    @Test
    void shouldThrowAnExceptionWhenGettingFromEmptyCell() {
        BillCell cell = BillCellImpl.empty(BillCash.FIVE_THOUSAND, 10);
        assertThatThrownBy(cell::get).isInstanceOf(AtmException.class);
    }


}