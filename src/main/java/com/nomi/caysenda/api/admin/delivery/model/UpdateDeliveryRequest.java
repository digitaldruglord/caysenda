package com.nomi.caysenda.api.admin.delivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryRequest {
    Integer id;
    Long amount;
    Long cost;
    Long fee;
    Long paid;
    String zhId;
    String viId;
    Double weight;
    Double volume;
}
