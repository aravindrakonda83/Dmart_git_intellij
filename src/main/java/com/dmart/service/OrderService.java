package com.dmart.service;

import com.dmart.model.CartItem;
import com.dmart.model.Order;
import com.dmart.model.User;

import java.util.List;
import java.util.Map;

public interface OrderService {
    void placeOrder(User user, Map<Integer, CartItem> cart);
    List<Order> getOrderHistory(int userId);

    List<Order> getOrderHistory(int userId, String search, int page, int pageSize);
    boolean hasNextPage(int userId, String search, int page, int pageSize);

}
