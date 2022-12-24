package com.nomi.caysenda.extension.model;

import java.util.List;

public class ExtensionOrder {
    Integer id;
    Integer orderId;
    String fullName;
    String phone;
    List<ExtensionOrderProduct> products;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ExtensionOrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<ExtensionOrderProduct> products) {
        this.products = products;
    }
}
