package ru.otus.solid.atm;

/**
 * Интерфейс фабрики стратегий снятия
 */
public interface CashRetrieveStrategyFactory {
    CashRetrieveStrategy createStrategy();
}
