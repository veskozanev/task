package com.highpriority.task.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.highpriority.task.dto.Product;

/**
 * Repository for managing CRUD operations for products in the database.
 */
@Repository
public class ProductRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a product from the database based on product ID.
     * 
     * @param productId The ID of the product to retrieve.
     * @return The found Product object, or null if no product is found with the given ID.
     */
    public Product findProductById(int productId) {
        final String sql = "SELECT * FROM ProductPricing WHERE ProductID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ProductRowMapper(), productId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Product ID: " + productId + " not found");
            return null; // Returning null to indicate not found
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle other exceptions gracefully
        }
    }

    /**
     * Retrieves the product name by the product ID.
     * 
     * @param productId The ID of the product.
     * @return The name of the product or "Unknown Product" if not found.
     */
    public String findProductNameById(int productId) {
        final String sql = "SELECT ProductName FROM Products WHERE ProductID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new ProductNameRowMapper(), productId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Product name for ID: " + productId + " not found");
            return "Unknown Product";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Product";
        }
    }

    /**
     * RowMapper for converting SQL result sets into Product objects.
     */
    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setProductId(rs.getInt("ProductID"));
            product.setUnitCostEUR(rs.getBigDecimal("UnitCostEUR"));
            product.setMarkup(rs.getString("Markup"));
            product.setPromotion(rs.getString("Promotion"));
            return product;
        }
    }

    /**
     * RowMapper for extracting product names from SQL result sets.
     */
    private static class ProductNameRowMapper implements RowMapper<String> {
        @Override
        public String mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("ProductName");
        }
    }
}
