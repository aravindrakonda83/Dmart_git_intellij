package com.dmart.service;

import com.dmart.dao.ProductDAO;
import com.dmart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public List<Product> listAll() {
        return productDAO.getAllProducts();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productDAO.getProductsByCategory(category);
    }
}
