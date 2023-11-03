package org.indexinvesting.portfoliocalculator;

import java.math.BigDecimal;

public class Position {
    // тикер (может быть нужно использовать ISIN качестве идентификатора?)
    private String ticker;
    // кол-во акций в портфеле
    private long numberOfPositionUnits;
    // цена за единицу
    private BigDecimal unitPrice;
    // общая цена позиции
    private BigDecimal positionPrice;

    public BigDecimal getPositionPrice() {
        return positionPrice;
    }

    public Position(String ticker, long numberOfPositionUnits, BigDecimal unitPrice) {
        this.ticker = ticker;
        this.numberOfPositionUnits = numberOfPositionUnits;
        this.unitPrice = unitPrice;
        this.positionPrice = unitPrice.multiply(BigDecimal.valueOf(numberOfPositionUnits));
    }

    @Override
    public String toString() {
        return "Позиция { " +
                "тикер = '" + ticker + '\'' +
                ", количество = " + numberOfPositionUnits +
                ", цена за единицу = " + unitPrice +
                ", общая цена позиции = " + positionPrice +
                " }";
    }
}
