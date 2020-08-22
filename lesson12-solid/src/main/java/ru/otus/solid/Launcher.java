package ru.otus.solid;

import ru.otus.solid.atm.AtmValidator;
import ru.otus.solid.atm.BillCell;
import ru.otus.solid.atm.impl.AtmValidatorImpl;
import ru.otus.solid.atm.impl.BillCellImpl;
import ru.otus.solid.atm.impl.CellAtmStorageImpl;
import ru.otus.solid.impl.AtmImpl;
import ru.otus.solid.types.BillCash;
import ru.otus.solid.types.CashPutStrategyType;
import ru.otus.solid.types.CashRetrieveStrategyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Launcher {
    public static void main(String[] args) {
        final AtmValidator validator=new AtmValidatorImpl();
        final List<BillCell> billCells = List.of(
                BillCellImpl.withAmount(BillCash.FIVE_THOUSAND, 10,8),
                BillCellImpl.withAmount(BillCash.TWO_THOUSAND, 15,10),
                BillCellImpl.withAmount(BillCash.ONE_THOUSAND, 20,13),
                BillCellImpl.withAmount(BillCash.FIVE_HUNDRED, 30,25),
                BillCellImpl.withAmount(BillCash.TWO_HUNDRED, 40,30),
                BillCellImpl.withAmount(BillCash.ONE_HUNDRED, 50,10)
        );
        final AtmImpl atm = new AtmImpl(new CellAtmStorageImpl(billCells, validator));
        System.out.println("atm.balance() = " + atm.balance());
        System.out.println("Retrieving 55700 = " + atm.getCash(55700, CashRetrieveStrategyType.MIN_BILLS_QUANTITY));
        System.out.println("atm.balance() after retrieve = " + atm.balance());
        List<BillCash> bills = new ArrayList<>();
        bills.addAll(Collections.nCopies(3,BillCash.FIVE_THOUSAND));
        bills.addAll(Collections.nCopies(2,BillCash.TWO_THOUSAND));
        bills.addAll(Collections.nCopies(8,BillCash.ONE_HUNDRED));
        System.out.println("Putting = " + atm.putAndReturnUnprocessed(bills, CashPutStrategyType.ALL_OR_NOTHING));
        System.out.println("atm.balance() after put = " + atm.balance());

    }
}
