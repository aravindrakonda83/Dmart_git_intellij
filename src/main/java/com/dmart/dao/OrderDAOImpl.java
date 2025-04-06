package com.dmart.dao;

import com.dmart.model.Order;
import com.dmart.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private JdbcTemplate jdbc;

    // Save a new order and return the generated order ID
    @Override
    public int saveOrder(Order order) {
        String sql = "INSERT INTO orders(user_id, total_amount, status) VALUES (?, ?, ?)";
        jdbc.update(sql, order.getUserId(), order.getTotalAmount(), order.getStatus() != null ? order.getStatus() : "Pending");
        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    @Override
    public void saveOrderItem(OrderItem item) {
        jdbc.update("INSERT INTO order_items(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)",
                item.getOrderId(), item.getProductId(), item.getQuantity(), item.getPrice());
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbc.query(sql, new Object[]{userId}, this::mapOrder);
    }

    @Override
    public List<Order> getUserOrdersWithFilter(int userId, String search, int offset, int limit) {
        String sql = """
            SELECT * FROM orders 
            WHERE user_id = ? 
              AND (CAST(id AS CHAR) LIKE ? OR status LIKE ?)
            ORDER BY order_date DESC
            LIMIT ? OFFSET ?
        """;

        String searchPattern = "%" + (search != null ? search : "") + "%";

        return jdbc.query(sql, new Object[]{
                userId, searchPattern, searchPattern, limit, offset
        }, this::mapOrder);
    }

    private Order mapOrder(ResultSet rs, int rowNum) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setStatus(rs.getString("status")); // ✅ New field mapping
        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        return o;
    }

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
}
