package com.nomi.caysenda.extension.model.request;

import java.util.List;

public class ExtensionUpToWeb {
    Integer shopId;
    Integer categoryId;
    List<Integer> providers;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getProviders() {
        return providers;
    }

    public void setProviders(List<Integer> providers) {
        this.providers = providers;
    }
}
