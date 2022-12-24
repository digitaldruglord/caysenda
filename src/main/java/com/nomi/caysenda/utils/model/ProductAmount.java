package com.nomi.caysenda.utils.model;

public class ProductAmount {
    Long amount;
    Long price;
    Integer quantity;

    public ProductAmount() {
    }

    public ProductAmount(Long amount, Long price, Integer quantity) {
        this.amount = amount;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
