package com.nomi.caysenda.controller.requests.cart;

import java.util.List;

public class CartRequest {
    Integer productId;
    List<CartDetailtRequest> detailts;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<CartDetailtRequest> getDetailts() {
        return detailts;
    }

    public void setDetailts(List<CartDetailtRequest> detailts) {
        this.detailts = detailts;
    }
}
