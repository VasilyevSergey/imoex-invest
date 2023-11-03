package org.indexinvesting.getindex;

public class Security {
    // Тикер
    private String secId;
    // Короткое имя
    private String shortName;
    // Вчерашняя цена
    private double prevPrice;
    // Размер лота
    private int lotSize;
    // Полное имя
    private String secName;

    public Security(String secId, String shortName, Double prevPrice, int lotSize, String secName) {
        this.secId = secId;
        this.shortName = shortName;
        this.prevPrice = prevPrice;
        this.lotSize = lotSize;
        this.secName = secName;
    }

    @Override
    public String toString() {
        return "Security{" +
                "secId='" + secId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", prevPrice=" + prevPrice +
                ", lotSize=" + lotSize +
                ", secName='" + secName + '\'' +
                '}';
    }
}
