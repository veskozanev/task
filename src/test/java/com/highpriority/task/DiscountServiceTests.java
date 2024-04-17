package com.highpriority.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import com.highpriority.task.dto.Client;
import com.highpriority.task.service.DiscountService;

class DiscountServiceTests {
    
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void calculateDiscounts_BasicDiscount() {
        // Setup
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5")); // 5%
        client.setVolumeDiscount10k(new BigDecimal("0"));
        client.setVolumeDiscount30k(new BigDecimal("0"));

        BigDecimal total = new BigDecimal("500"); // Below any volume discount threshold

        // Execution
        BigDecimal discount = discountService.calculateClientDiscounts(client, total);

        // Assertion
        assertEquals(new BigDecimal("25.00"), discount, "The basic discount should be 5% of 500");
    }

    @Test
    void calculateDiscounts_VolumeDiscount10k() {
        // Setup
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5")); // 5%
        client.setVolumeDiscount10k(new BigDecimal("10")); // 10%
        client.setVolumeDiscount30k(new BigDecimal("15")); // 15%

        BigDecimal total = new BigDecimal("15000"); // Above 10k but below 30k

        // Execution
        BigDecimal discount = discountService.calculateClientDiscounts(client, total);

        // Assertion
        assertEquals(new BigDecimal("2250.00"), discount, "The total discount should be 15% basic plus 10% volume above 10k");
    }

    @Test
    void calculateDiscounts_VolumeDiscount30k() {
        // Setup
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5")); // 5%
        client.setVolumeDiscount10k(new BigDecimal("10")); // 10%
        client.setVolumeDiscount30k(new BigDecimal("15")); // 15%

        BigDecimal total = new BigDecimal("40000"); // Above 30k

        // Execution
        BigDecimal discount = discountService.calculateClientDiscounts(client, total);

        // Assertion
        assertEquals(new BigDecimal("8000.00"), discount, "The total discount should be 20% (5% basic plus 15% volume above 30k)");
    }

    @Test
    void calculateDiscounts_AtVolumeDiscount10kThreshold() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("10"));
        client.setVolumeDiscount30k(new BigDecimal("15"));

        BigDecimal total = new BigDecimal("10000"); // Exactly at 10k threshold

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("500.00"), discount, "The total discount should be 10% at the 10k threshold.");
    }

    @Test
    void calculateDiscounts_JustAboveVolumeDiscount10kThreshold() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("10"));
        client.setVolumeDiscount30k(new BigDecimal("15"));

        BigDecimal total = new BigDecimal("10000.01"); // Exactly at 10.01k threshold

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("1500.00"), discount, "The total discount should be 10% at the 10k threshold.");
    }

    @Test
    void calculateDiscounts_AtVolumeDiscount30kThreshold() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("10"));
        client.setVolumeDiscount30k(new BigDecimal("15"));

        BigDecimal total = new BigDecimal("30000"); // Exactly at 30k threshold

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("4500.00"), discount, "The total discount should be 15% at the 30k threshold.");
    }

    @Test
    void calculateDiscounts_JustBelowVolumeDiscount10kThreshold() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("10"));
        client.setVolumeDiscount30k(new BigDecimal("15"));

        BigDecimal total = new BigDecimal("9999.99"); // Just below the 10k threshold

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("500.00"), discount, "The total discount should only include the basic 5% discount just below 10k.");
    }

    @Test
    void calculateDiscounts_JustBelowVolumeDiscount30kThreshold() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("10"));
        client.setVolumeDiscount30k(new BigDecimal("15"));

        BigDecimal total = new BigDecimal("29999.99"); // Just below the 30k threshold

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("4500.00"), discount, "The total discount should include 5% basic plus 10% volume, just below 30k.");
    }

    @Test
    void calculateDiscounts_VerySmallAmount() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("0"));
        client.setVolumeDiscount30k(new BigDecimal("0"));

        BigDecimal total = new BigDecimal("10.00"); // Very small amount

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("0.50"), discount, "The total discount should be 5% of 10.");
    }

    @Test
    void calculateDiscounts_NegativeTotal() {
        Client client = new Client();
        client.setBasicDiscount(new BigDecimal("5"));
        client.setVolumeDiscount10k(new BigDecimal("0"));
        client.setVolumeDiscount30k(new BigDecimal("0"));

        BigDecimal total = new BigDecimal("-500"); // Negative value

        BigDecimal discount = discountService.calculateClientDiscounts(client, total);
        assertEquals(new BigDecimal("-25.00"), discount, "The total discount should be negative when the total is negative.");
    }


}
