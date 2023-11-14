package org.indexinvesting.portfoliocalculator;

import org.indexinvesting.securities.Security;

import java.math.BigDecimal;

public class Position {
    // ценная бумага
    private Security security;

    public Security getSecurity() {
        return security;
    }

    public long getNumberOfLots() {
        return numberOfLots;
    }

    // кол-во лотов в портфеле
    private long numberOfLots;
    // общая цена позиции
    private BigDecimal positionPrice;

    public BigDecimal getPositionPrice() {
        return positionPrice;
    }

    public Position(Security security, long numberOfLots) {
        this.security = security;
        this.numberOfLots = numberOfLots;
        this.positionPrice = security.getLotPrice().multiply(BigDecimal.valueOf(numberOfLots));
    }

    @Override
    public String toString() {
        return "Позиция { " +
                "ценная бумага = '" + security.toString() + '\'' +
                ", количество лотов = " + numberOfLots +
                ", общая цена позиции = " + positionPrice +
                " }";
    }
}
