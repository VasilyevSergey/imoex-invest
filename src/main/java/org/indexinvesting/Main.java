package org.indexinvesting;

import org.indexinvesting.getindex.ImoexCompositionGetter;
import org.indexinvesting.getindex.ImoexCompositionGetterFromCsv;
import org.indexinvesting.getindex.ImoexCompositionGetterFromSmartlab;
import org.indexinvesting.portfoliocalculator.PortfolioCalculator;
import org.indexinvesting.portfoliocalculator.PortfolioCalculatorImpl;
import org.indexinvesting.portfoliocalculator.Position;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ImoexCompositionGetter imoexCompositionGetter = new ImoexCompositionGetterFromCsv();
        imoexCompositionGetter.getImoexComposition();

        PortfolioCalculator calculator = new PortfolioCalculatorImpl();
        long targetPortfolioPrice = 100000L;
        List<Position> portfolio = calculator.calculate(BigDecimal.valueOf(targetPortfolioPrice));

        // сортируем по убыванию общей цены позиции и выводим
        portfolio.stream()
                .sorted(Comparator.comparing(Position::getPositionPrice))
                .toList()
                .reversed()
                .forEach(System.out::println);
    }
}