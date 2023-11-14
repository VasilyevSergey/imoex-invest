package org.indexinvesting.orders;

import org.indexinvesting.portfoliocalculator.Position;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class OrdersCreatorImpl implements OrdersCreator {

    @Override
    public List<Order> createOrders(List<Position> currentPortfolio, List<Position> targetPortfolio) {
        if (currentPortfolio == null) {
            currentPortfolio = Collections.emptyList();
        }

        if (targetPortfolio == null) {
            targetPortfolio = Collections.emptyList();
        }

        List<Order> orders = new ArrayList<>();
        orders.addAll(createOrdersForRebalanceExistingPositions(currentPortfolio, targetPortfolio));
        orders.addAll(createOrdersToSellDeletedPositions(currentPortfolio, targetPortfolio));
        orders.addAll(createOrdersForBuyNewPositions(currentPortfolio, targetPortfolio));

        // оставляем только заявки с ненулевым количеством лотов
        return orders.stream()
                .filter(order -> order.getLotSize() == 0)
                .collect(Collectors.toList());
    }

    /**
     * Формирование списка заявок для удаления устаревших позиций. Если в целевом портфеле не найдена позиция,
     * то формируется заявка на продажу этой позиции.
     * Пример: [a, b, c] -> [a, c] - должна быть создана заявка на продажу позиции b
     *
     * @param currentPortfolio текущий состав портфеля
     * @param targetPortfolio  целевой состав портфеля
     * @return список заявок на продажу
     */
    private List<Order> createOrdersToSellDeletedPositions(List<Position> currentPortfolio, List<Position> targetPortfolio) {
        List<Order> orders = new ArrayList<>();
        for (Position currentPosition : currentPortfolio) {
            Optional<Position> targetPosition = targetPortfolio.stream()
                    .filter(position -> Objects.equals(position.getSecurity(), currentPosition.getSecurity()))
                    .findFirst();

            if (targetPosition.isEmpty()) {
                Order sellPositionOrder = new Order(currentPosition.getSecurity(), currentPosition.getNumberOfLots(), Operation.SELL);
                orders.add(sellPositionOrder);
            }
        }
        return orders;
    }

    /**
     * Формирование списка заявок для покупки новых позиций. Если в текущем портфеле не найдена позиция,
     * то формируется заявка на покупку этой позиции.
     * Пример: [a, b, c] -> [a, b, c, d] - должна быть создана заявка на покупку позиции d
     *
     * @param currentPortfolio текущий состав портфеля
     * @param targetPortfolio  целевой состав портфеля
     * @return список заявок на покупку
     */
    private List<Order> createOrdersForBuyNewPositions(List<Position> currentPortfolio, List<Position> targetPortfolio) {
        List<Order> orders = new ArrayList<>();
        for (Position targetPosition : targetPortfolio) {
            Optional<Position> currentPosition = currentPortfolio.stream()
                    .filter(position -> Objects.equals(targetPosition.getSecurity(), position.getSecurity()))
                    .findFirst();

            if (currentPosition.isEmpty()) {
                Order buyPositionOrder = new Order(targetPosition.getSecurity(), targetPosition.getNumberOfLots(), Operation.BUY);
                orders.add(buyPositionOrder);
            }
        }
        return orders;
    }

    private List<Order> createOrdersForRebalanceExistingPositions(List<Position> currentPortfolio, List<Position> targetPortfolio) {
        List<Order> orders = new ArrayList<>();
        for (Position targetPosition : targetPortfolio) {
            Position currentPosition = currentPortfolio.stream()
                    .filter(position -> Objects.equals(targetPosition.getSecurity(), position.getSecurity()))
                    .findFirst()
                    .orElse(null);

            if (currentPosition != null) {
                Order order = createOrder(currentPosition, targetPosition);
                orders.add(order);
            }
        }
        return orders;
    }

    private Order createOrder(Position currentPosition, Position targetPosition) {
        long currentNumberOfLots = currentPosition.getNumberOfLots();
        long targetNumberOfLots = targetPosition.getNumberOfLots();

        if (currentNumberOfLots < 0 || targetNumberOfLots < 0) {
            String errorMessage = String.format("NumberOfLots can't be < 0; currentNumberOfLots = %s, targetNumberOfLots = %s",
                    currentNumberOfLots, targetNumberOfLots);
            throw new RuntimeException(errorMessage);
        }

        long diffNumberOfLots = targetNumberOfLots - currentNumberOfLots;
        if (diffNumberOfLots == 0) {
            // количество лотов позиции не изменилось, заявка не нужна
            return null;
        }

        Operation operation = getOperation(diffNumberOfLots);
        return new Order(targetPosition.getSecurity(), abs(diffNumberOfLots), operation);
    }

    private Operation getOperation(long diffNumberOfLots) {
        if (diffNumberOfLots < 0) {
            return Operation.SELL;
        } else if (diffNumberOfLots > 0) {
            return Operation.BUY;
        } else {
            return null;
        }
    }
}
