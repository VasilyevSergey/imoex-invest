package org.indexinvesting.getindex;

import java.math.BigDecimal;

public class Issuer {
    private String name;
    private String ticker;
    private Double weight;
    private BigDecimal price;

    public Issuer() {
    }

    public Issuer(String name, String ticker, Double weight, BigDecimal price) {
        this.name = name;
        this.ticker = ticker;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getWeight() {
        return weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Issuer{" +
                "name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                '}';
    }
}
