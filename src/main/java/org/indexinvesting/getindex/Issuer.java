package org.indexinvesting.getindex;

import org.indexinvesting.securities.Security;

public class Issuer {
    // ценная бумага
    private Security security;
    private Double weight;

    public Issuer() {
    }

    public Issuer(Security security, Double weight) {
        this.security = security;
        this.weight = weight;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Issuer{" +
                "security=" + security +
                ", weight=" + weight +
                '}';
    }
}
