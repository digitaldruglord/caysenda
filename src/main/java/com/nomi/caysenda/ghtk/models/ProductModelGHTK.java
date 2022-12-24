package com.nomi.caysenda.ghtk.models;

public class ProductModelGHTK {
    String name;
    long price;
    float weight;
    Integer quantity;

    public ProductModelGHTK() {
    }

    public ProductModelGHTK(String name, long price, float weight, Integer quantity) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.quantity = quantity;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
