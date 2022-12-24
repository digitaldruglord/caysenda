package com.nomi.caysenda.ghn.model.setting;

import com.nomi.caysenda.ghn.model.model.ShopModel;

import java.util.List;

public class GHNTokenModel {
    String name;
    String token;
    Integer shopId;
    List<ShopModel> shops;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public List<ShopModel> getShops() {
        return shops;
    }

    public void setShops(List<ShopModel> shops) {
        this.shops = shops;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
