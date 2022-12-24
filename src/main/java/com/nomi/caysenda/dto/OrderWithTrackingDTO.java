package com.nomi.caysenda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter

public class OrderWithTrackingDTO {
    Integer id;
    String fullName;
    String phoneNumber;
    Long productAmount;
    Long orderAmount;
    String billingEmail;
    String billingAddress;
    String billingCity;
    String billingDistrict;
    String billingWards;
    Date createDate;
    String productName;
    String viStatusCode;
    Integer trackingId;
    Integer countPackage;
    Integer packageReceived;
    Long cod;
    Long paid;
    String fullAddress;

    public OrderWithTrackingDTO(Integer id, String fullName, String phoneNumber, Long productAmount, Long orderAmount, String billingEmail, String billingAddress, String billingCity, String billingDistrict, String billingWards, Date createDate, String productName, String viStatusCode, Integer trackingId, Integer countPackage, Integer packageReceived, Long cod, Long paid) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.productAmount = productAmount;
        this.orderAmount = orderAmount;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingDistrict = billingDistrict;
        this.billingWards = billingWards;
        this.createDate = createDate;
        this.productName = productName;
        this.viStatusCode = viStatusCode;
        this.trackingId = trackingId;
        this.countPackage = countPackage;
        this.packageReceived = packageReceived;
        this.cod = cod;
        this.paid = paid;
    }

}
