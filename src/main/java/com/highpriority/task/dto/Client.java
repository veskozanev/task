package com.highpriority.task.dto;

import java.math.BigDecimal;

public class Client {
    private long id;
    private String name; 
    private BigDecimal basicDiscount; // Discount in decimal form e.g 0.05 for 5%
    private BigDecimal volumeDiscount10k;
    private BigDecimal volumeDiscount30k;

    // Constructor
    public Client() { 
    }
 

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBasicDiscount() {
        return basicDiscount;
    }

    public BigDecimal getVolumeDiscount10k() {
        return volumeDiscount10k;
    }

    public BigDecimal getVolumeDiscount30k() {
        return volumeDiscount30k;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBasicDiscount(BigDecimal basicDiscount) {
        this.basicDiscount = basicDiscount;
    }


    public void setVolumeDiscount10k(BigDecimal volumeDiscount10k) {
        this.volumeDiscount10k = volumeDiscount10k;
    }

    public void setVolumeDiscount30k(BigDecimal volumeDiscount30k) {
        this.volumeDiscount30k = volumeDiscount30k;
    }
}
