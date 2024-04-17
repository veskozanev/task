package com.highpriority.task.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;
import com.highpriority.task.dto.Product;

@Service
public class PricingService {
    // Constant for percentage calculations to enhance readability.
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * Calculates the standard unit price for a product based on its markup.
     * The markup can be either a percentage or a fixed amount.
     *
     * @param product the product for which the unit price is calculated
     * @return the calculated standard unit price, rounded to two decimal places
     */
    public BigDecimal calculateStandardUnitPrice(Product product) {
        if (product.getMarkup().endsWith("%")) {
            // Convert percentage markup into a multiplier for the unit cost.
            BigDecimal markupPercent = new BigDecimal(product.getMarkup().replace("%", ""));
            BigDecimal markupAmount = product.getUnitCostEUR()
                .multiply(markupPercent)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            return product.getUnitCostEUR().add(markupAmount).setScale(2, RoundingMode.HALF_UP);
        } else {
            // Handle fixed amount markup.
            BigDecimal markupAmount = new BigDecimal(product.getMarkup().replace(" EUR/unit", ""));
            return product.getUnitCostEUR().add(markupAmount).setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Calculates the promotional unit price based on the product's promotion type and quantity.
     * Handles different promotions such as discounts and "Buy 2, get 3rd free".
     *
     * @param product the product for which the promotional price is calculated
     * @param quantity the quantity of the product ordered, affecting certain promotions
     * @return the promotional unit price, rounded to five decimal places for precision
     */
    public BigDecimal calculatePromotionalUnitPrice(Product product, int quantity) {
        if ("none".equals(product.getPromotion())) {
            // No promotion, use standard pricing.
            return calculateStandardUnitPrice(product);
        } else if ("30% off".equals(product.getPromotion())) {
            // Apply a percentage discount for the promotion.
            BigDecimal discount = new BigDecimal("0.30");
            return calculateStandardUnitPrice(product).multiply(BigDecimal.ONE.subtract(discount))
                    .setScale(5, RoundingMode.HALF_UP);
        } else if ("Buy 2, get 3rd free".equals(product.getPromotion())) {
            // Calculate price for "Buy 2, get 3rd free" taking into account total units.
            int setsOfThree = quantity / 3;
            int remainder = quantity % 3;
            BigDecimal promotionalPrice = calculateStandardUnitPrice(product).multiply(
                new BigDecimal(setsOfThree * 2 + remainder))
                .divide(new BigDecimal(quantity), 5, RoundingMode.HALF_UP);
            return promotionalPrice;
        }
        // If promotion type is unknown, throw an exception to notify the developers/maintainers.
        throw new IllegalArgumentException("Unknown promotion type: " + product.getPromotion());
    }
}
