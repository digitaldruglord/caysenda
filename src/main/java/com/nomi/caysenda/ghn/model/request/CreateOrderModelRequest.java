package com.nomi.caysenda.ghn.model.request;

import com.nomi.caysenda.ghn.entity.GHNOrderItemEntity;

import java.util.List;

public class CreateOrderModelRequest {
    Integer payment_type_id;
    String order_code;
    String note;
    String required_note;
    String return_phone;
    String return_address;
    Integer return_district_id;
    String return_ward_code;
    String client_order_code;
    String to_name;
    String to_phone;
    String to_address;
    String to_province;
    String to_ward_code;
    Integer to_district_id;
    Long cod_amount;
    String content;
    Integer weight;
    Integer length;
    Integer width;
    Integer height;
    Integer pick_station_id;
    Integer deliver_station_id;
    Long insurance_value;
    Integer service_id;
    Integer service_type_id;
    Long order_value;
    String coupon;
    List<Integer> pick_shift;
    List<GHNOrderItemEntity> items;
    Integer orderId;
    Long fee;
    Long insurance_fee;

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getInsurance_fee() {
        return insurance_fee;
    }

    public void setInsurance_fee(Long insurance_fee) {
        this.insurance_fee = insurance_fee;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTo_province() {
        return to_province;
    }

    public void setTo_province(String to_province) {
        this.to_province = to_province;
    }

    public Integer getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(Integer payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRequired_note() {
        return required_note;
    }

    public void setRequired_note(String required_note) {
        this.required_note = required_note;
    }

    public String getReturn_phone() {
        return return_phone;
    }

    public void setReturn_phone(String return_phone) {
        this.return_phone = return_phone;
    }

    public String getReturn_address() {
        return return_address;
    }

    public void setReturn_address(String return_address) {
        this.return_address = return_address;
    }

    public Integer getReturn_district_id() {
        return return_district_id;
    }

    public void setReturn_district_id(Integer return_district_id) {
        this.return_district_id = return_district_id;
    }

    public String getReturn_ward_code() {
        return return_ward_code;
    }

    public void setReturn_ward_code(String return_ward_code) {
        this.return_ward_code = return_ward_code;
    }

    public String getClient_order_code() {
        return client_order_code;
    }

    public void setClient_order_code(String client_order_code) {
        this.client_order_code = client_order_code;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_phone() {
        return to_phone;
    }

    public void setTo_phone(String to_phone) {
        this.to_phone = to_phone;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_ward_code() {
        return to_ward_code;
    }

    public void setTo_ward_code(String to_ward_code) {
        this.to_ward_code = to_ward_code;
    }

    public Integer getTo_district_id() {
        return to_district_id;
    }

    public void setTo_district_id(Integer to_district_id) {
        this.to_district_id = to_district_id;
    }

    public Long getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(Long cod_amount) {
        this.cod_amount = cod_amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPick_station_id() {
        return pick_station_id;
    }

    public void setPick_station_id(Integer pick_station_id) {
        this.pick_station_id = pick_station_id;
    }

    public Integer getDeliver_station_id() {
        return deliver_station_id;
    }

    public void setDeliver_station_id(Integer deliver_station_id) {
        this.deliver_station_id = deliver_station_id;
    }

    public Long getInsurance_value() {
        return insurance_value;
    }

    public void setInsurance_value(Long insurance_value) {
        this.insurance_value = insurance_value;
    }

    public Integer getService_id() {
        return service_id;
    }

    public void setService_id(Integer service_id) {
        this.service_id = service_id;
    }

    public Integer getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(Integer service_type_id) {
        this.service_type_id = service_type_id;
    }

    public Long getOrder_value() {
        return order_value;
    }

    public void setOrder_value(Long order_value) {
        this.order_value = order_value;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public List<Integer> getPick_shift() {
        return pick_shift;
    }

    public void setPick_shift(List<Integer> pick_shift) {
        this.pick_shift = pick_shift;
    }

    public List<GHNOrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<GHNOrderItemEntity> items) {
        this.items = items;
    }
}
