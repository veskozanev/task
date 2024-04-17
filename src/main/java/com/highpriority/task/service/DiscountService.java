package com.highpriority.task.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.highpriority.task.dto.Client;

/**
 * Service to calculate various types of discounts for clients based on order totals.
 */
@Service
public class DiscountService {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal TEN_THOUSAND = new BigDecimal("10000");
    private static final BigDecimal THIRTY_THOUSAND = new BigDecimal("30000");

    /**
     * Calculates the total discount for a client based on the order total and client-specific discount rules.
     *
     * @param client The client for whom the discount is being calculated.
     * @param total The total amount of the order before discounts.
     * @return The total discount amount to be applied.
     */
    public BigDecimal calculateClientDiscounts(Client client, BigDecimal total) {
        // Basic discount is calculated as a percentage of the total order amount
        BigDecimal basicDiscountAmount = calculatePercentageDiscount(total, client.getBasicDiscount());

        // Volume discount depends on the order total falling into specific ranges
        BigDecimal volumeDiscountAmount = calculateVolumeDiscount(total, client);

        // The total discount is the sum of basic and volume discounts
        BigDecimal totalDiscount = basicDiscountAmount.add(volumeDiscountAmount);
        return totalDiscount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Helper method to calculate a percentage discount.
     *
     * @param amount The amount on which the discount is calculated.
     * @param discountRate The discount rate as a percentage.
     * @return The calculated discount.
     */
    private BigDecimal calculatePercentageDiscount(BigDecimal amount, BigDecimal discountRate) {
        return amount.multiply(discountRate).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates additional volume discounts based on thresholds defined in client's discount structure.
     *
     * @param total The total order amount before any discounts.
     * @param client The client data containing discount thresholds.
     * @return The calculated volume discount.
     */
    private BigDecimal calculateVolumeDiscount(BigDecimal total, Client client) {
        if (total.compareTo(THIRTY_THOUSAND) > 0) {
            return calculatePercentageDiscount(total, client.getVolumeDiscount30k());
        } else if (total.compareTo(TEN_THOUSAND) > 0) {
            return calculatePercentageDiscount(total, client.getVolumeDiscount10k());
        }
        return BigDecimal.ZERO;
    }
}
