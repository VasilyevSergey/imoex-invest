package org.indexinvesting.orders;

import org.indexinvesting.portfoliocalculator.Position;

import java.util.List;

public interface OrdersCreator {
    /**
     * Формирование списка ордеров на покупку/продажу акций исходя текущего и целевого состава портфеля
     *
     * @param currentPortfolio текущий состав портфеля
     * @param targetPortfolio  целевой состав портфеля
     * @return список ордеров на покупку/продажу
     */
    List<Order> createOrders(List<Position> currentPortfolio, List<Position> targetPortfolio);
}
