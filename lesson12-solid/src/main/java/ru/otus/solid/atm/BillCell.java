package ru.otus.solid.atm;

import ru.otus.solid.types.BillCash;

/**
 * Интерйфейс денежной ячейки
 */
public interface BillCell extends Cell {
    /**
     * @return тип купюр доступных для данной ячейки
     */
    BillCash cashType();
}
