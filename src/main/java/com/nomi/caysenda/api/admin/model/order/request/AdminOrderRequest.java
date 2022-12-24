package com.nomi.caysenda.api.admin.model.order.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;
@Getter
@Setter
public class AdminOrderRequest {
    private Integer id;
    private String billingFullName;
    private String billingPhoneNumber;
    private String billingEmail;
    private String orderComment;
    private String billingAddress;
    private String billingCity;
    private String billingDistrict;
    private String billingWards;
    private Long ship;
    private Long productAmount;
    private Long orderAmount;
    private String status;
    private Long refunded;
    private Long paid;
    private String method;
    private Long incurredCost;
    String note;
    String discountType;
    Double discountValue;
    List<AdminOrderDetailt> detailts;
    Long cost;
    String source;
    String sourceId;
}
