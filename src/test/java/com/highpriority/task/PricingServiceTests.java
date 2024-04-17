package com.highpriority.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.highpriority.task.service.PricingService; 
import com.highpriority.task.dto.Product;

class PricingServiceTests {
    
    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @Test
    void calculateStandardUnitPrice_PercentageMarkup() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("25%");

        BigDecimal expectedPrice = new BigDecimal("125.00");
        BigDecimal calculatedPrice = pricingService.calculateStandardUnitPrice(product);
        assertEquals(expectedPrice, calculatedPrice, "The standard unit price should account for a 25% markup.");
    }

    @Test
    void calculateStandardUnitPrice_FixedMarkup() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("30 EUR/unit");

        BigDecimal expectedPrice = new BigDecimal("130.00");
        BigDecimal calculatedPrice = pricingService.calculateStandardUnitPrice(product);
        assertEquals(expectedPrice, calculatedPrice, "The standard unit price should account for a fixed €30 markup.");
    }

    @Test
    void calculatePromotionalUnitPrice_NonePromotion() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("20%");
        product.setPromotion("none");

        BigDecimal expectedPrice = new BigDecimal("120.00");
        BigDecimal calculatedPrice = pricingService.calculatePromotionalUnitPrice(product, 1);
        assertEquals(expectedPrice, calculatedPrice, "Promotional price should equal standard price with no promotion.");
    }

    @Test
    void calculatePromotionalUnitPrice_DiscountPromotion() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("10%");
        product.setPromotion("30% off");

        BigDecimal expectedPrice = new BigDecimal("77.00"); // 110 * 0.7 = 77
        BigDecimal calculatedPrice = pricingService.calculatePromotionalUnitPrice(product, 1);
        assertEquals(expectedPrice, calculatedPrice, "Promotional price should include a 30% discount.");
    }

    @Test
    void calculatePromotionalUnitPrice_Buy2Get1FreePromotion() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("0%");
        product.setPromotion("Buy 2, get 3rd free");

        BigDecimal expectedPrice = new BigDecimal("66.67");
        BigDecimal calculatedPrice = pricingService.calculatePromotionalUnitPrice(product, 3);
        assertEquals(expectedPrice, calculatedPrice.setScale(2, RoundingMode.HALF_UP), "Promotional price should calculate 'Buy 2, get 3rd free' correctly.");
    }

    @Test
    void calculatePromotionalUnitPrice_UnknownPromotion() {
        Product product = new Product();
        product.setUnitCostEUR(new BigDecimal("100.00"));
        product.setMarkup("10%");
        product.setPromotion("50% off");

        assertThrows(IllegalArgumentException.class, () -> {
            pricingService.calculatePromotionalUnitPrice(product, 1);
        }, "Should throw an exception for unknown promotion types.");
    }

    @Test
void calculateStandardUnitPrice_ZeroMarkup() {
    Product product = new Product();
    product.setUnitCostEUR(new BigDecimal("100.00"));
    product.setMarkup("0%");

    BigDecimal expectedPrice = new BigDecimal("100.00");
    BigDecimal calculatedPrice = pricingService.calculateStandardUnitPrice(product);
    assertEquals(expectedPrice, calculatedPrice, "The standard unit price should remain unchanged with zero markup.");
}

@Test
void calculatePromotionalUnitPrice_NegativeMarkup() {
    Product product = new Product();
    product.setUnitCostEUR(new BigDecimal("100.00"));
    product.setMarkup("-10%");

    BigDecimal expectedPrice = new BigDecimal("90.00");
    BigDecimal calculatedPrice = pricingService.calculateStandardUnitPrice(product);
    assertEquals(expectedPrice, calculatedPrice, "The standard unit price should decrease by 10% with negative markup.");
}

@Test
void calculatePromotionalUnitPrice_BoundaryBuy2Get1Free() {
    Product product = new Product();
    product.setUnitCostEUR(new BigDecimal("100.00"));
    product.setMarkup("0%");
    product.setPromotion("Buy 2, get 3rd free");

    // Testing with exactly 2, which should not trigger the third free
    BigDecimal expectedPrice = new BigDecimal("100.00");
    BigDecimal calculatedPrice = pricingService.calculatePromotionalUnitPrice(product, 2);
    assertEquals(expectedPrice, calculatedPrice, "Promotional price should not apply as only 2 items purchased.");
}

@Test
void calculatePromotionalUnitPrice_LargeQuantity() {
    Product product = new Product();
    product.setUnitCostEUR(new BigDecimal("100.00"));
    product.setMarkup("10%");
    product.setPromotion("30% off");

    BigDecimal expectedPrice = new BigDecimal("77.00");
    BigDecimal calculatedPrice = pricingService.calculatePromotionalUnitPrice(product, 10000);
    assertEquals(expectedPrice, calculatedPrice, "Promotional price should handle large quantities correctly.");
}

@Test
void calculatePromotionalUnitPrice_ZeroQuantity() {
    Product product = new Product();
    product.setUnitCostEUR(new BigDecimal("100.00"));
    product.setMarkup("10%");
    product.setPromotion("Buy 2, get 3rd free");

    assertThrows(ArithmeticException.class, () -> {
        pricingService.calculatePromotionalUnitPrice(product, 0);
    }, "Should throw an exception due to division by zero.");
}

}
