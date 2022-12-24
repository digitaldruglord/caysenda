package com.nomi.caysenda.options.model;

import java.util.List;

public class Geo2IP {
    Boolean enable;
    List<GeoCountry> list;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<GeoCountry> getList() {
        return list;
    }

    public void setList(List<GeoCountry> list) {
        this.list = list;
    }
}
