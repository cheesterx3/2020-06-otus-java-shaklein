package ru.otus.solid.atm;

/**
 * Интерфейс наличности
 */
public interface Cash {
    /**
     * @return номинал
     */
    int nominal();

    /**
     * Получение общей суммы наличности
     *
     * @param cashCount кол-во купюр
     * @return полная сумма
     */
    default int total(int cashCount) {
        return nominal() * cashCount;
    }

}
