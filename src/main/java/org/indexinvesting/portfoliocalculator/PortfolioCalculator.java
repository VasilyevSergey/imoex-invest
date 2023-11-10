package org.indexinvesting.portfoliocalculator;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioCalculator {

    /**
     * Расчет целевого состава портфеля исходя из актуального состава индекса МосБиржи, целевой цены портфеля
     * и текущего состава портфеля.
     *
     * @param portfolioTargetPrice целевая цена портфеля
     * @return целевой состав портфеля
     */
    List<Position> calculate(BigDecimal portfolioTargetPrice);

    /**
     * Вычисление цены портфеля
     *
     * @param portfolio портфель
     * @return цена портфеля
     */
    BigDecimal calculatePortfolioPrice(List<Position> portfolio);
}
