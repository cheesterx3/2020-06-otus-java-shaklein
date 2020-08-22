package ru.otus.solid.atm.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.solid.atm.AtmValidator;
import ru.otus.solid.atm.BillCell;
import ru.otus.solid.atm.CashPutStrategy;
import ru.otus.solid.atm.CashRetrieveStrategy;
import ru.otus.solid.types.BillCash;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CellAtmStorageImplTest {
    public static final int FTH1_CAPACITY = 5;
    public static final int FTH2_CAPACITY = 4;
    public static final int CAPACITY = 10;
    private CellAtmStorageImpl storage;
    private AtmValidator validator;
    private CashPutStrategy putStrategy;
    private CashRetrieveStrategy retrieveStrategy;

    @BeforeEach
    void setUp() {
        validator = mock(AtmValidator.class);
        List<BillCell> billCells = List.of(
                BillCellImpl.empty(BillCash.FIVE_THOUSAND, FTH1_CAPACITY),
                BillCellImpl.empty(BillCash.FIVE_THOUSAND, FTH2_CAPACITY),
                BillCellImpl.empty(BillCash.TWO_THOUSAND, CAPACITY),
                BillCellImpl.empty(BillCash.ONE_THOUSAND, CAPACITY),
                BillCellImpl.empty(BillCash.FIVE_HUNDRED, CAPACITY),
                BillCellImpl.empty(BillCash.TWO_HUNDRED, CAPACITY),
                BillCellImpl.empty(BillCash.ONE_HUNDRED, CAPACITY)
        );
        putStrategy = mock(CashPutStrategy.class);
        when(putStrategy.calcProcessable(anyCollection(), any()))
                .thenAnswer(invocation -> invocation.getArguments()[0]);
        retrieveStrategy = mock(CashRetrieveStrategy.class);
        storage = new CellAtmStorageImpl(billCells, validator);
    }

    @Test
    void shouldReturnCorrectFreeSpace() {
        assertThat(storage.availableSpace(BillCash.FIVE_THOUSAND)).isEqualTo(FTH1_CAPACITY + FTH2_CAPACITY);
        assertThat(storage.availableSpace(BillCash.TWO_THOUSAND)).isEqualTo(CAPACITY);
        assertThat(storage.availableSpace(BillCash.ONE_THOUSAND)).isEqualTo(CAPACITY);
        assertThat(storage.availableSpace(BillCash.FIVE_HUNDRED)).isEqualTo(CAPACITY);
        assertThat(storage.availableSpace(BillCash.TWO_HUNDRED)).isEqualTo(CAPACITY);
        assertThat(storage.availableSpace(BillCash.ONE_HUNDRED)).isEqualTo(CAPACITY);
    }

    @Test
    void shouldCorrectlyPutBillAndDecreaseItsCapacityAndIncreaseItAmount() {
        storage.put(BillCash.ONE_HUNDRED);
        assertThat(storage.availableSpace(BillCash.ONE_HUNDRED)).isEqualTo(CAPACITY - 1);
        assertThat(storage.availableCash(BillCash.ONE_HUNDRED)).isEqualTo(1);
        storage.put(BillCash.FIVE_THOUSAND);
        assertThat(storage.availableSpace(BillCash.FIVE_THOUSAND)).isEqualTo(FTH1_CAPACITY + FTH2_CAPACITY - 1);
        assertThat(storage.availableCash(BillCash.FIVE_THOUSAND)).isEqualTo(1);
    }

    @Test
    void shouldCorrectlyPutCollectionOfBillsAndDecreaseCapacity() {
        int expected = FTH1_CAPACITY + FTH2_CAPACITY;
        int billCount = FTH1_CAPACITY + FTH2_CAPACITY;
        storage.putAll(Collections.nCopies(billCount, BillCash.FIVE_THOUSAND), putStrategy);
        assertThat(storage.availableSpace(BillCash.FIVE_THOUSAND)).isZero();
        assertThat(storage.availableCash(BillCash.FIVE_THOUSAND)).isEqualTo(expected);
    }

    @Test
    void shouldReturnCorrectAvailableCash() {
        assertThat(storage.availableCash(BillCash.FIVE_THOUSAND)).isZero();
        assertThat(storage.availableCash(BillCash.TWO_THOUSAND)).isZero();
        assertThat(storage.availableCash(BillCash.ONE_THOUSAND)).isZero();
        assertThat(storage.availableCash(BillCash.FIVE_HUNDRED)).isZero();
        assertThat(storage.availableCash(BillCash.TWO_HUNDRED)).isZero();
        assertThat(storage.availableCash(BillCash.ONE_HUNDRED)).isZero();
    }

    @Test
    void shouldCorrectlyRetrieveBillIfAvailable() {
        int capacity = 10;
        int amount = 8;
        List<BillCash> cashesToRetrieve = Collections.nCopies(amount, BillCash.FIVE_THOUSAND);
        storage = new CellAtmStorageImpl(List.of(BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, capacity, capacity)), validator);
        when(retrieveStrategy.retrieve(eq(amount), any())).thenReturn(cashesToRetrieve);

        Collection<BillCash> cashes = storage.get(amount, retrieveStrategy);
        assertThat(cashes).isEqualTo(cashesToRetrieve);
        assertThat(storage.availableCash(BillCash.FIVE_THOUSAND)).isEqualTo(capacity - amount);
        assertThat(storage.availableSpace(BillCash.FIVE_THOUSAND)).isEqualTo(amount);
    }


    @Test
    void shouldCorrectlyCountBalance() {
        assertThat(storage.balance()).isZero();
        storage.put(BillCash.FIVE_THOUSAND);
        assertThat(storage.balance()).isEqualTo(BillCash.FIVE_THOUSAND.nominal());
        storage.put(BillCash.TWO_THOUSAND);
        assertThat(storage.balance()).isEqualTo(BillCash.FIVE_THOUSAND.nominal() + BillCash.TWO_THOUSAND.nominal());
        storage.put(BillCash.TWO_THOUSAND);
        assertThat(storage.balance()).isEqualTo(BillCash.FIVE_THOUSAND.nominal() + BillCash.TWO_THOUSAND.total(2));

    }
}