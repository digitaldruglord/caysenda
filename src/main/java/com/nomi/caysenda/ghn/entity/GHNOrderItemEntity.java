package com.nomi.caysenda.ghn.entity;

public class GHNOrderItemEntity {
    String name;
    String code;
    Integer quantity;
    Long price;

    public GHNOrderItemEntity() {
    }

    public GHNOrderItemEntity(String name, String code, Integer quantity) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
