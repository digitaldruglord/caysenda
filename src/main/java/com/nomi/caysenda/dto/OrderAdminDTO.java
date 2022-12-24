package com.nomi.caysenda.dto;

import com.nomi.caysenda.entity.TrackingOrderEntity;
import com.nomi.caysenda.ghn.model.responses.OrderSummary;
import com.nomi.caysenda.lazada.entity.LazadaOrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class OrderAdminDTO {
    Integer id;
    String fullName;
    String phoneNumber;
    String status;
    String method;
    Long productAmount;
    Long orderAmount;
    Long ship;
    Long cost;
    String billingEmail;
    String billingAddress;
    String billingCity;
    String billingDistrict;
    String billingWards;
    String fullAddress;
    Date createDate;
    Long incurredCost;
    Long profit;
    Long paid;
    String host;
    String orderCode;
    Long cod;
    Float profitMargin;
    String note;
    String adminNote;
    List<TrackingOrderEntity> tracking;
    List<OrderSummary> ghn;
    String cashflowStatus;
    LazadaOrderEntity lazadaOrder;



    public OrderAdminDTO() {
    }

    public OrderAdminDTO(Integer id, String fullName, String phoneNumber, String status, String method, Long productAmount, Long orderAmount, Long ship, Long cost, String billingEmail, String billingAddress, String billingCity, String billingDistrict, String billingWards, Date createDate, Long incurredCost, Long paid, String host, String orderCode, String note, String adminNote, String cashflowStatus) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.method = method;
        this.productAmount = productAmount;
        this.orderAmount = orderAmount;
        this.ship = ship;
        this.cost = cost;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingDistrict = billingDistrict;
        this.billingWards = billingWards;
        this.createDate = createDate;
        this.incurredCost = incurredCost;
        this.paid = paid;
        this.host = host;
        this.orderCode = orderCode;
        this.note = note;
        this.adminNote = adminNote;
        this.cashflowStatus = cashflowStatus;
    }

    public OrderAdminDTO(Integer id, String fullName, String phoneNumber, String status, String method, Long productAmount, Long orderAmount, Long ship, Long cost, String billingEmail, String billingAddress, String billingCity, String billingDistrict, String billingWards, Date createDate, Long incurredCost, Long paid, String host, String orderCode, String note, String adminNote) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.method = method;
        this.productAmount = productAmount;
        this.orderAmount = orderAmount;
        this.ship = ship;
        this.cost = cost;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingDistrict = billingDistrict;
        this.billingWards = billingWards;
        this.createDate = createDate;
        this.incurredCost = incurredCost;
        this.paid = paid;
        this.host = host;
        this.orderCode = orderCode;
        this.note = note;
        this.adminNote = adminNote;
    }

    public OrderAdminDTO(Integer id, String fullName, String phoneNumber, String status, String method, Long productAmount, Long orderAmount, Long ship, Long cost, String billingEmail, String billingAddress, String billingCity, String billingDistrict, String billingWards, Date createDate, Long incurredCost, Long paid, String host, String orderCode, String note) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.method = method;
        this.productAmount = productAmount;
        this.orderAmount = orderAmount;
        this.ship = ship;
        this.cost = cost;
        this.billingEmail = billingEmail;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingDistrict = billingDistrict;
        this.billingWards = billingWards;
        this.createDate = createDate;
        this.incurredCost = incurredCost;
        this.paid = paid;
        this.host = host;
        this.orderCode = orderCode;
        this.note = note;
    }

}
