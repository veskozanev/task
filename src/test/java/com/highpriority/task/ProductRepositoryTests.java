package com.highpriority.task;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.highpriority.task.dto.Product;
import com.highpriority.task.repository.ProductRepository;

import org.springframework.dao.EmptyResultDataAccessException;

class ProductRepositoryTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindProductByIdFound() {
        Product expectedProduct = new Product();
        expectedProduct.setProductId(100);
        expectedProduct.setUnitCostEUR(new BigDecimal("1.00"));
        expectedProduct.setMarkup("10%");
        expectedProduct.setPromotion("none");

        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(100)))
                .thenReturn(expectedProduct);

        Product actualProduct = productRepository.findProductById(100);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getProductId(), actualProduct.getProductId());
        assertEquals(expectedProduct.getUnitCostEUR(), actualProduct.getUnitCostEUR());
        assertEquals(expectedProduct.getMarkup(), actualProduct.getMarkup());
        assertEquals(expectedProduct.getPromotion(), actualProduct.getPromotion());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindProductByIdNotFound() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(101)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Product actualProduct = productRepository.findProductById(101);

        assertNull(actualProduct);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindProductNameByIdFound() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(100)))
                .thenReturn("Test Product");

        String productName = productRepository.findProductNameById(100);

        assertNotNull(productName);
        assertEquals("Test Product", productName);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFindProductNameByIdNotFound() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(103)))
                .thenThrow(new EmptyResultDataAccessException(1));

        String productName = productRepository.findProductNameById(103);

        assertEquals("Unknown Product", productName);
    }
}
