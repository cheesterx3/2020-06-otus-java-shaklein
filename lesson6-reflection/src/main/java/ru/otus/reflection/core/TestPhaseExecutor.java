package ru.otus.reflection.core;

/**
 * Интерфейс запуска единичной фазы тестирования. Это может быть как пре/пост-исполнитель, так и сам тест
 */
public interface TestPhaseExecutor {
    /**
     * Выполнение фазы тестирования
     * @param object экземпляр объекта
     * @return результат запуска фазы
     */
    DetailTestInfo execute(Object object);

    /**
     *
     * @return наименование фазы
     */
    String getName();
}
