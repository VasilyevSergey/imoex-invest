package org.indexinvesting;

import org.indexinvesting.portfoliocalculator.PortfolioCalculator;
import org.indexinvesting.portfoliocalculator.PortfolioCalculatorImpl;
import org.indexinvesting.portfoliocalculator.Position;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Расчёт из файла, работает
        PortfolioCalculator calculator = new PortfolioCalculatorImpl();
        long targetPortfolioPrice = 30000L;
        List<Position> portfolio = calculator.calculate(BigDecimal.valueOf(targetPortfolioPrice));

        // Сортируем по убыванию общей цены позиции и выводим
        portfolio.stream()
                .sorted(Comparator.comparing(Position::getPositionPrice))
                .toList()
                .reversed()
                .forEach(System.out::println);
    }
}