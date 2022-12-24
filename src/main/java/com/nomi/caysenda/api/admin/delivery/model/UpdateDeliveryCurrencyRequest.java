package com.nomi.caysenda.api.admin.delivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryCurrencyRequest {
    Integer id;
    Long amount;
    String status;

}
