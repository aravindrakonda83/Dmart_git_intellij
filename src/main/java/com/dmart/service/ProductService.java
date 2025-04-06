package com.dmart.service;

import com.dmart.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> listAll();
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    void addProduct(Product product);

    void deleteProduct(int id);

}
