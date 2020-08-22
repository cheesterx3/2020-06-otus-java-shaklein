package ru.otus.solid.atm;

/**
 * Интерфейс ячейки хранения
 */
public interface Cell {

    /**
     * @return размер ячейки
     */
    int capacity();

    /**
     * @return кол-во занятых элементов ячейки
     */
    int amount();

    /**
     * Метод добавления содержимого ячейки
     *
     * @throws ru.otus.solid.exceptions.AtmException в случае попытки положить в полную ячейку
     */
    void put();

    /**
     * Метод взятия из содержимого ячейки
     *
     * @throws ru.otus.solid.exceptions.AtmException в случае попытки взять из пустой ячейки
     */
    void get();

    /**
     * @return объём свободных ячеек
     */
    default int freeSpace() {
        return capacity() - amount();
    }

    /**
     * @return {@code true} если ячейка пустая
     */
    default boolean isEmpty() {
        return amount() == 0;
    }

    /**
     * @return {@code true} если в ячейке есть свободное место
     */
    default boolean hasSpace() {
        return capacity() - amount() > 0;
    }


}
