package com.dmart.model;

import lombok.Data;

@Data
public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
}
