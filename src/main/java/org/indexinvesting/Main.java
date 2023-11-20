package org.indexinvesting;

import org.indexinvesting.correctionfactor.CorrectionFactor;
import org.indexinvesting.correctionfactor.CorrectionFactorGetter;
import org.indexinvesting.correctionfactor.CorrectionFactorGetterFromCsv;
import org.indexinvesting.orders.Order;
import org.indexinvesting.orders.OrdersCreator;
import org.indexinvesting.orders.OrdersCreatorImpl;
import org.indexinvesting.portfoliocalculator.PortfolioCalculator;
import org.indexinvesting.portfoliocalculator.PortfolioCalculatorImpl;
import org.indexinvesting.portfoliocalculator.Position;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        PortfolioCalculator calculator = new PortfolioCalculatorImpl();
        long targetPortfolioPrice = 30000L;
        List<Position> portfolio = calculator.calculate(BigDecimal.valueOf(targetPortfolioPrice));

        // Сортируем по убыванию общей цены позиции и выводим
        portfolio.stream()
                .filter(position -> position.getNumberOfLots() != 0)
                .sorted(Comparator.comparing(Position::getPositionPrice))
                .toList()
                .reversed()
                .forEach(System.out::println);

        System.out.println("\n===================\n");

        OrdersCreator ordersCreator = new OrdersCreatorImpl();
        List<Order> orders = ordersCreator.createOrders(new ArrayList<>(), portfolio);
        orders.forEach(System.out::println);
    }
}