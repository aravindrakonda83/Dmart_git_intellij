package com.dmart.service;

import com.dmart.dao.OrderDAO;
import com.dmart.model.CartItem;
import com.dmart.model.Order;
import com.dmart.model.OrderItem;
import com.dmart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public void placeOrder(User user, Map<Integer, CartItem> cart) {
        double total = cart.values().stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(total);
        order.setStatus("Pending"); // ✅ Set default status

        int orderId = orderDAO.saveOrder(order);

        for (CartItem item : cart.values()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(item.getProduct().getId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());

            orderDAO.saveOrderItem(orderItem);
        }
    }


    @Override
    public List<Order> getOrderHistory(int userId) {
        List<Order> orders = orderDAO.findOrdersByUserId(userId);
        for (Order order : orders) {
            order.setItems(orderDAO.findItemsByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public List<Order> getOrderHistory(int userId, String search, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<Order> orders = orderDAO.getUserOrdersWithFilter(userId, search, offset, pageSize);
        for (Order order : orders) {
            order.setItems(orderDAO.findItemsByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public boolean hasNextPage(int userId, String search, int page, int pageSize) {
        int offset = page * pageSize;
        return orderDAO.getUserOrdersWithFilter(userId, search, offset, pageSize).size() > 0;
    }
}
