package com.dmart.dao;

import com.dmart.model.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
}
