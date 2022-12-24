package com.nomi.caysenda.options.model;

public class ShipSetting {
    Boolean enable;
    String shipType;
    Double shipValue;
    Long fee;
    Long extraFee;

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(Long extraFee) {
        this.extraFee = extraFee;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Double getShipValue() {
        return shipValue;
    }

    public void setShipValue(Double shipValue) {
        this.shipValue = shipValue;
    }
}
