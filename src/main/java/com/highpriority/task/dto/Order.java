package com.highpriority.task.dto;

import java.math.BigDecimal;
import java.util.List;

public class Order {
    private Client client;
    private List<OrderLine> orderLines;
    private BigDecimal totalBeforeDiscounts;
    private BigDecimal additionalDiscount;
    private BigDecimal orderTotalAmount;

    // Constructors
    public Order(Client client, List<OrderLine> orderLines, BigDecimal totalBeforeDiscounts, BigDecimal additionalDiscount, BigDecimal orderTotalAmount) {
        this.client = client;
        this.orderLines = orderLines;
        this.totalBeforeDiscounts = totalBeforeDiscounts;
        this.additionalDiscount = additionalDiscount;
        this.orderTotalAmount = orderTotalAmount;
    }

    public Order(Client client, List<OrderLine> orderLines) {
        this.client = client;
        this.orderLines = orderLines; 
    }

    // Getters
    public Client getClient() {
        return client;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public BigDecimal getTotalBeforeDiscounts() {
        return totalBeforeDiscounts;
    }

    public BigDecimal getAdditionalDiscount() {
        return additionalDiscount;
    }

    public BigDecimal getOrderTotalAmount() {
        return orderTotalAmount;
    }

    // Setters
    public void setClient(Client client) {
        this.client = client;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void setTotalBeforeDiscounts(BigDecimal totalBeforeDiscounts) {
        this.totalBeforeDiscounts = totalBeforeDiscounts;
    }

    public void setAdditionalDiscount(BigDecimal additionalDiscount) {
        this.additionalDiscount = additionalDiscount;
    }

    public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }
}