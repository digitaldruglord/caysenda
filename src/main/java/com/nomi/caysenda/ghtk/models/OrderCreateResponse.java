package com.nomi.caysenda.ghtk.models;

public class OrderCreateResponse {
    private String partner_id;
    private String label;
    Integer area;
    Integer fee;
    Integer insurance_fee;
    String estimated_pick_time;
    String estimated_deliver_time;
    String status_id;

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getInsurance_fee() {
        return insurance_fee;
    }

    public void setInsurance_fee(Integer insurance_fee) {
        this.insurance_fee = insurance_fee;
    }

    public String getEstimated_pick_time() {
        return estimated_pick_time;
    }

    public void setEstimated_pick_time(String estimated_pick_time) {
        this.estimated_pick_time = estimated_pick_time;
    }

    public String getEstimated_deliver_time() {
        return estimated_deliver_time;
    }

    public void setEstimated_deliver_time(String estimated_deliver_time) {
        this.estimated_deliver_time = estimated_deliver_time;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }
}
