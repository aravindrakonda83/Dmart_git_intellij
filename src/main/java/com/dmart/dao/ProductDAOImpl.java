package com.dmart.dao;

import com.dmart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productMapper = new RowMapper<>() {
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setCategory(rs.getString("category"));
            product.setPrice(rs.getDouble("price"));
            product.setMrp(rs.getDouble("mrp"));
            product.setUnitOptions(rs.getString("unit_options"));
            product.setImageUrl(rs.getString("image_url"));
            return product;
        }
    };

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productMapper);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        return jdbcTemplate.query(sql, productMapper, category);
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (name, category, price, mrp, unit_options, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getMrp(),
                product.getUnitOptions(),
                product.getImageUrl()
        );
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
