package com.nomi.caysenda.controller.requests.cart;

public class CartDetailtRequest {
    Integer variantId;
    Integer quantity;

    public CartDetailtRequest() {
    }

    public CartDetailtRequest(Integer variantId, Integer quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
