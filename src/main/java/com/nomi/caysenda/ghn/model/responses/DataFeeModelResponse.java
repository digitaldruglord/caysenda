package com.nomi.caysenda.ghn.model.responses;

public class DataFeeModelResponse {
    Long total;
    Long service_fee;
    Long insurance_fee;
    Integer pick_station_fee;
    Integer coupon_value;
    Integer r2s_fee;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getService_fee() {
        return service_fee;
    }

    public void setService_fee(Long service_fee) {
        this.service_fee = service_fee;
    }

    public Long getInsurance_fee() {
        return insurance_fee;
    }

    public void setInsurance_fee(Long insurance_fee) {
        this.insurance_fee = insurance_fee;
    }

    public Integer getPick_station_fee() {
        return pick_station_fee;
    }

    public void setPick_station_fee(Integer pick_station_fee) {
        this.pick_station_fee = pick_station_fee;
    }

    public Integer getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(Integer coupon_value) {
        this.coupon_value = coupon_value;
    }

    public Integer getR2s_fee() {
        return r2s_fee;
    }

    public void setR2s_fee(Integer r2s_fee) {
        this.r2s_fee = r2s_fee;
    }
}
