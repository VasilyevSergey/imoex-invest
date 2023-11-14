package org.indexinvesting.securities;

import java.math.BigDecimal;

public class Security {
    // Тикер
    private String secId;
    // Короткое имя
    private String shortName;
    // Полное имя
    private String secName;
    // Вчерашняя цена
    private double prevClosePrice;
    // Размер лота
    private int lotSize;
    // Стоимость лота
    private BigDecimal lotPrice;


    public Security(String secId, String shortName, Double prevClosePrice, int lotSize, String secName) {
        this.secId = secId;
        this.shortName = shortName;
        this.prevClosePrice = prevClosePrice;
        this.lotSize = lotSize;
        this.secName = secName;
        this.lotPrice = new BigDecimal(lotSize).multiply(BigDecimal.valueOf(prevClosePrice));
    }

    @Override
    public String toString() {
        return "Security{" +
                "secId='" + secId + "}";
    }

    public String getSecId() {
        return secId;
    }

    public BigDecimal getLotPrice() {
        return lotPrice;
    }

    public String getShortName() {
        return shortName;
    }
}
