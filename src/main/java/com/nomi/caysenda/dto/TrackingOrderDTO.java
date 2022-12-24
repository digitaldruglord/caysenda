package com.nomi.caysenda.dto;

import java.util.Date;

public class TrackingOrderDTO {
    Integer id;
    String name;
    String orderCode;
    String status;
    String statusZh;
    Integer orderId;
    Date receiptDate;
    Boolean received;

    public TrackingOrderDTO(Integer id, String name, String orderCode, String status, String statusZh, Integer orderId, Date receiptDate, Boolean received) {
        this.id = id;
        this.name = name;
        this.orderCode = orderCode;
        this.status = status;
        this.statusZh = statusZh;
        this.orderId = orderId;
        this.receiptDate = receiptDate;
        this.received = received;
    }

    public TrackingOrderDTO(Integer id, String name, String orderCode, String status, String statusZh, Integer orderId, Date receiptDate) {
        this.id = id;
        this.name = name;
        this.orderCode = orderCode;
        this.status = status;
        this.statusZh = statusZh;
        this.orderId = orderId;
        this.receiptDate = receiptDate;
    }

    public TrackingOrderDTO(Integer id, String name, String orderCode, String status, String statusZh, Integer orderId) {
        this.id = id;
        this.name = name;
        this.orderCode = orderCode;
        this.status = status;
        this.statusZh = statusZh;
        this.orderId = orderId;
    }

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusZh() {
        return statusZh;
    }

    public void setStatusZh(String statusZh) {
        this.statusZh = statusZh;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
