package com.dmart.dao;

import com.dmart.model.Order;
import com.dmart.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private JdbcTemplate jdbc;

    // Save a new order and return generated order ID
    @Override
    public int saveOrder(Order order) {
        String sql = "INSERT INTO orders(user_id, total_amount, status) VALUES (?, ?, ?)";
        jdbc.update(sql, order.getUserId(), order.getTotalAmount(), order.getStatus() != null ? order.getStatus() : "Pending");
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    // Save a single order item
    @Override
    public void saveOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_items(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, item.getOrderId(), item.getProductId(), item.getQuantity(), item.getPrice());
    }

    // Fetch all orders for a user
    @Override
    public List<Order> findOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbc.query(sql, new Object[]{userId}, this::mapOrder);
    }

    // Fetch orders with filtering, pagination, and search
    @Override
    public List<Order> getUserOrdersWithFilters(int userId, String search, String status, String date, int offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE user_id = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);

        String searchPattern = "%" + (search != null ? search : "") + "%";
        sql.append(" AND (CAST(id AS CHAR) LIKE ? OR status LIKE ?)");
        params.add(searchPattern);
        params.add(searchPattern);

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }

        if (date != null && !date.isEmpty()) {
            sql.append(" AND DATE(order_date) = ?");
            params.add(date);
        }

        sql.append(" ORDER BY order_date DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return jdbc.query(sql.toString(), params.toArray(), this::mapOrder);
    }

    // Map a single order from ResultSet
    private Order mapOrder(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        return order;
    }

    // Fetch items of a specific order
    @Override
    public List<OrderItem> findItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbc.query(sql, new Object[]{orderId}, (rs, rowNum) -> {
            OrderItem item = new OrderItem();
            item.setId(rs.getInt("id"));
            item.setOrderId(rs.getInt("order_id"));
            item.setProductId(rs.getInt("product_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getDouble("price"));
            return item;
        });
    }
    @Override
    public Order findOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        List<Order> list = jdbc.query(sql, new Object[]{orderId}, this::mapOrder);
        return list.isEmpty() ? null : list.get(0);
    }

}
