package com.dmart.dao;

import com.dmart.model.Order;
import com.dmart.model.OrderItem;

import java.util.List;

public interface OrderDAO {
    int saveOrder(Order order);
    void saveOrderItem(OrderItem item);
    List<Order> findOrdersByUserId(int userId);

    List<OrderItem> findItemsByOrderId(int orderId);
    List<Order> getUserOrdersWithFilters(int userId, String search, String status, String date, int offset, int limit);
    Order findOrderById(int orderId);

}

