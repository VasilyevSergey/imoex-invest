package org.indexinvesting.portfoliocalculator;

import org.indexinvesting.getindex.ImoexCompositionGetter;
import org.indexinvesting.getindex.ImoexCompositionGetterFromCsv;
import org.indexinvesting.getindex.Issuer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class PortfolioCalculatorImpl implements PortfolioCalculator {

    private static final BigDecimal STEP_IN_RUR = BigDecimal.valueOf(1000L);
    private ImoexCompositionGetter imoexCompositionGetter = new ImoexCompositionGetterFromCsv();

    @Override
    public List<Position> calculate(BigDecimal portfolioTargetPrice) {
        List<Issuer> imoexComposition = imoexCompositionGetter.getImoexComposition();

        // Считаем портфели для каждой цены в диапазоне [portfolioPrice - 50%; portfolioPrice + 50%] с заданным шагом
        Map<BigDecimal /*error*/, List<Position> /*portfolio*/> portfolioByError = new HashMap<>();

        List<Position> portfolio;
        BigDecimal portfolioPrice;
        BigDecimal error; // отклонение цены портфеля от цены целевого портфеля
        BigDecimal minPortfolioTargetPrice = portfolioTargetPrice.divide(BigDecimal.TWO, RoundingMode.HALF_UP);
        BigDecimal maxPortfolioTargetPrice = portfolioTargetPrice.multiply(BigDecimal.TWO);
        BigDecimal currentPortfolioTargetPrice = minPortfolioTargetPrice;

        while (currentPortfolioTargetPrice.compareTo(maxPortfolioTargetPrice) < 1) {
            portfolio = calculatePreliminaryPortfolio(currentPortfolioTargetPrice, imoexComposition);
            portfolioPrice = calculatePortfolioPrice(portfolio);
            error = portfolioTargetPrice.subtract(portfolioPrice);

            // учитываем только портфели, цена которых не выше целевой
            if (error.signum() == 1) {
                portfolioByError.putIfAbsent(error, portfolio);
            }

            currentPortfolioTargetPrice = currentPortfolioTargetPrice.add(STEP_IN_RUR);
        }

        if (portfolioByError.isEmpty()) {
            throw new RuntimeException("Не удалось рассчитать мапу портфелей");
        }

        if (portfolioByError.size() == 1) {
            return portfolioByError.get(0);
        }

        // находим портфель с минимальным отклонением
        List<Position> optimalPortfolio = portfolioByError.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey())
                .orElseThrow()
                .getValue();

        System.out.println("Цена оптимального портфеля: " + calculatePortfolioPrice(optimalPortfolio).toString());

        return optimalPortfolio;
    }

    @Override
    public BigDecimal calculatePortfolioPrice(List<Position> portfolio) {
        BigDecimal portfolioPrice = BigDecimal.ZERO;
        for (Position position : portfolio) {
            portfolioPrice = portfolioPrice.add(position.getPositionPrice());
        }
        return portfolioPrice;
    }

    /**
     * Вычисление предварительного состава портфеля. На маленьких суммах составляет не оптимальный портфель из-за округлений
     *
     * @param portfolioTargetPrice целевая цена портфеля
     * @return предварительный состав портфеля
     */
    private List<Position> calculatePreliminaryPortfolio(BigDecimal portfolioTargetPrice, List<Issuer> imoexComposition) {
        if (portfolioTargetPrice == null || Objects.equals(portfolioTargetPrice, BigDecimal.ZERO)) {
            return Collections.emptyList();
        }

        List<Position> portfolio = new ArrayList<>();

        for (Issuer issuer : imoexComposition) {
            // Вычисление суммарной цены позиции = целевая цена портфеля * вес позиции
            BigDecimal positionTargetPrice = portfolioTargetPrice.multiply(BigDecimal.valueOf(issuer.getWeight()));
            // Вычисление количества штук позиции = суммарная цена позиции / цена за 1 единицу позиции, округление стандартное
            long numberOfPositionUnits = positionTargetPrice.divide(issuer.getPrice(), RoundingMode.HALF_UP).longValue();

            Position position = new Position(issuer.getTicker(), numberOfPositionUnits, issuer.getPrice());
            portfolio.add(position);
        }

        return portfolio;
    }
}
