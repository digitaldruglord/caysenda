package com.nomi.caysenda.api.admin.delivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDeliveryRequest {
    String fullName;
    String phoneNumber;
    String productName;
    Integer quantity;
    String address;
    Double weight;
    Double volume;
}
