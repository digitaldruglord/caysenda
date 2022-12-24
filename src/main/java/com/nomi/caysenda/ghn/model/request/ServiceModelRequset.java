package com.nomi.caysenda.ghn.model.request;

public class ServiceModelRequset {
    Integer shop_id;
    Integer from_district;
    Integer to_district;

    public ServiceModelRequset() {
    }

    public ServiceModelRequset(Integer shop_id, Integer from_district, Integer to_district) {
        this.shop_id = shop_id;
        this.from_district = from_district;
        this.to_district = to_district;
    }

    public Integer getShop_id() {
        return shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public Integer getFrom_district() {
        return from_district;
    }

    public void setFrom_district(Integer from_district) {
        this.from_district = from_district;
    }

    public Integer getTo_district() {
        return to_district;
    }

    public void setTo_district(Integer to_district) {
        this.to_district = to_district;
    }
}
