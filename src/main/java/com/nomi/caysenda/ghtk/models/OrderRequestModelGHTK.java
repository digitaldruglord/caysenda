package com.nomi.caysenda.ghtk.models;

import java.util.List;

public class OrderRequestModelGHTK {
    List<ProductModelGHTK> products;
    OrderModelGHTK order;

    public OrderRequestModelGHTK() {
    }

    public OrderRequestModelGHTK(List<ProductModelGHTK> products, OrderModelGHTK order) {
        this.products = products;
        this.order = order;
    }

    public List<ProductModelGHTK> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModelGHTK> products) {
        this.products = products;
    }

    public OrderModelGHTK getOrder() {
        return order;
    }

    public void setOrder(OrderModelGHTK order) {
        this.order = order;
    }
}
