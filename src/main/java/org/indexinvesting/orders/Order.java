package org.indexinvesting.orders;

import org.indexinvesting.securities.Security;

public class Order {
    // бумага
    Security security;
    // количество лотов
    long lotSize;
    // покупка/продажа
    Operation operation;

    public long getLotSize() {
        return lotSize;
    }

    @Override
    public String toString() {
        return "Order{" +
                "security=" + security.getSecId() +
                ", lotSize=" + lotSize +
                ", operation=" + operation +
                '}';
    }

    public Order(Security security, long lotSize, Operation operation) {
        this.security = security;
        this.lotSize = lotSize;
        this.operation = operation;
    }
}
