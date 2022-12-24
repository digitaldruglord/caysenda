package com.nomi.caysenda.ghn.model.responses;

import com.nomi.caysenda.ghn.model.model.ShopModel;

import java.util.List;

public class DataStoreModelResponse {
    Integer last_offset;
    List<ShopModel> shops;

    public Integer getLast_offset() {
        return last_offset;
    }

    public void setLast_offset(Integer last_offset) {
        this.last_offset = last_offset;
    }

    public List<ShopModel> getShops() {
        return shops;
    }

    public void setShops(List<ShopModel> shops) {
        this.shops = shops;
    }
}
