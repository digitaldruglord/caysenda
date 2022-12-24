package com.nomi.caysenda.ghn.model.responses;

import java.util.Date;

public class OrderSummary {
    Integer id;
    String status;
    String orderCodeGhn;
    String orderCode;
    Long amount;
    Long cod;
    Long totalFee;
    String name;
    String statusName;
    String color;
    Date modifiedDate;

    public OrderSummary(Integer id, String status, String orderCodeGhn, String orderCode, Long amount, Long cod, Long totalFee, String name, Date modifiedDate) {
        this.id = id;
        this.status = status;
        this.orderCodeGhn = orderCodeGhn;
        this.orderCode = orderCode;
        this.amount = amount;
        this.cod = cod;
        this.totalFee = totalFee;
        this.name = name;
        this.modifiedDate = modifiedDate;
    }



    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderCodeGhn() {
        return orderCodeGhn;
    }

    public void setOrderCodeGhn(String orderCodeGhn) {
        this.orderCodeGhn = orderCodeGhn;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }
}
