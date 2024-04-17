package com.highpriority.task.dto;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private BigDecimal unitCostEUR;  
    private String markup;
    private String promotion;

    // Getters
    public int getProductId() {
        return productId;
    }

    public BigDecimal getUnitCostEUR() {
        return unitCostEUR;
    }

    public String getMarkup() {
        return markup;
    }
    
    public String getPromotion() {
        return promotion;
    }

    // Setters
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setUnitCostEUR(BigDecimal unitCostEUR) {
        this.unitCostEUR = unitCostEUR;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
}
