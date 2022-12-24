package com.nomi.caysenda.dto;

import com.nomi.caysenda.entity.OrderDetailtEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class PrintOrderDTO {
    Integer orderId;
    String fullName;
    String phoneNumber;
    String email;
    String fullAddress;
    String note;
    Date createDate;
    String status;
    String method;
    Long ship;
    Long productAmount;
    Long orderAmount;
    Long refunded;
    String discountType;
    Double discountValue;
    String orderCode;
    Long cod;
    Long paid;
    List<PrintOrderDetailDTO> detailts;

}
