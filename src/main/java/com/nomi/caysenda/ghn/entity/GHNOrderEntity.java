package com.nomi.caysenda.ghn.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ghnOrder")
public class GHNOrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String status;
    String orderCodeGhn;
    String orderCode;
    Integer orderId;
    String sortCode;
    String token;
    String name;
    String phoneNumber;
    Long amount;
    Long cod;
    String to_province;
    Long totalFee;
    Long insurance_fee;
    Long main_service;
    String productName;
    Date modifiedDate;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getInsurance_fee() {
        return insurance_fee;
    }

    public void setInsurance_fee(Long insurance_fee) {
        this.insurance_fee = insurance_fee;
    }

    public Long getMain_service() {
        return main_service;
    }

    public void setMain_service(Long main_service) {
        this.main_service = main_service;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getTo_province() {
        return to_province;
    }

    public void setTo_province(String to_province) {
        this.to_province = to_province;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
