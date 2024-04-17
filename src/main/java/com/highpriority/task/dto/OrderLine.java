package com.highpriority.task.dto;

import java.math.BigDecimal;

public class OrderLine {
    private Product product;
    private int quantity;
    private BigDecimal standardUnitPrice;
    private BigDecimal promotionalUnitPrice;
    private BigDecimal lineTotal;

    // Constructors
    public OrderLine(Product product, int quantity, BigDecimal standardUnitPrice, BigDecimal promotionalUnitPrice, BigDecimal lineTotal) {
        this.product = product;
        this.quantity = quantity;
        this.standardUnitPrice = standardUnitPrice;
        this.promotionalUnitPrice = promotionalUnitPrice;
        this.lineTotal = lineTotal;
    }

    // Getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getStandardUnitPrice() {
        return standardUnitPrice;
    }

    public BigDecimal getPromotionalUnitPrice() {
        return promotionalUnitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    // Setters
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStandardUnitPrice(BigDecimal standardUnitPrice) {
        this.standardUnitPrice = standardUnitPrice;
    }

    public void setPromotionalUnitPrice(BigDecimal promotionalUnitPrice) {
        this.promotionalUnitPrice = promotionalUnitPrice;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
