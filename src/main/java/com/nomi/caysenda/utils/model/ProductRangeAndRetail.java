package com.nomi.caysenda.utils.model;

public class ProductRangeAndRetail {
    Boolean retail;
    Integer range;
    Integer rangeActive;
    Integer totalQuantity;
    Integer totalQuantityActive;
    Long amount;
    Long amountActive;

    Boolean active;



    public ProductRangeAndRetail() {
    }

    public ProductRangeAndRetail(Boolean retail) {
        this.retail = retail;
    }

    public ProductRangeAndRetail(Boolean retail, Integer range) {
        this.retail = retail;
        this.range = range;
    }

    public ProductRangeAndRetail(Boolean retail, Integer range, Integer totalQuantity) {
        this.retail = retail;
        this.range = range;
        this.totalQuantity = totalQuantity;
    }

    public Integer getRangeActive() {
        return rangeActive;
    }

    public void setRangeActive(Integer rangeActive) {
        this.rangeActive = rangeActive;
    }

    public Long getAmountActive() {
        return amountActive;
    }

    public void setAmountActive(Long amountActive) {
        this.amountActive = amountActive;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getTotalQuantityActive() {
        return totalQuantityActive;
    }

    public void setTotalQuantityActive(Integer totalQuantityActive) {
        this.totalQuantityActive = totalQuantityActive;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Boolean getRetail() {
        return retail;
    }

    public void setRetail(Boolean retail) {
        this.retail = retail;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }
}
