package ru.otus.solid.atm;

/**
 * Интерфейс фабрики ртсатегии зачисления
 */
public interface CashPutStrategyFactory {
    CashPutStrategy createStrategy();
}
