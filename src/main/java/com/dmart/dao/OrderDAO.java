package com.dmart.dao;

import com.dmart.model.Order;
import com.dmart.model.OrderItem;

import java.util.List;

public interface OrderDAO {
    int saveOrder(Order order);
    void saveOrderItem(OrderItem item);
    List<Order> findOrdersByUserId(int userId);
    List<Order> getUserOrdersWithFilter(int userId, String search, int offset, int limit);
    List<OrderItem> findItemsByOrderId(int orderId);
}

