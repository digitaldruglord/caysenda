package com.nomi.caysenda.extension.dto;

import javax.persistence.Column;

public class ExtensionOrderDTO {
    Integer id;
    Integer orderId;
    String status;
    String fullName;
    String phoneNumber;

    public ExtensionOrderDTO() {
    }

    public ExtensionOrderDTO(Integer id, Integer orderId, String status, String fullName, String phoneNumber) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
