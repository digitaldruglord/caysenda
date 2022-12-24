package com.nomi.caysenda.api.admin.delivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryOrderRequest {
    Integer id;
    Long amount;
    Long cost;
    Long fee;
    Long paid;
    String zhId;
    String viId;
}
