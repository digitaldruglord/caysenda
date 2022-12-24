package com.nomi.caysenda.api1688.model;

import java.util.List;

public class Variant1688Sku {
    String discount;
    String discountPrice;
    String price;
    String retailPrice;
    Long canBookCount;
    Long saleCount;
    String skuProps;
    String skuMap;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Long getCanBookCount() {
        return canBookCount;
    }

    public void setCanBookCount(Long canBookCount) {
        this.canBookCount = canBookCount;
    }

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public String getSkuProps() {
        return skuProps;
    }

    public void setSkuProps(String skuProps) {
        this.skuProps = skuProps;
    }

    public String getSkuMap() {
        return skuMap;
    }

    public void setSkuMap(String skuMap) {
        this.skuMap = skuMap;
    }
}
