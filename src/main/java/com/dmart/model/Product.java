package com.dmart.model;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double mrp;
    private String unitOptions;
    private String imageUrl;

}