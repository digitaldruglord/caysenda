package com.nomi.caysenda.api.admin.delivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDeliveryOrderStatusRequest {
    Integer id;
    String status;
    String note;
}
