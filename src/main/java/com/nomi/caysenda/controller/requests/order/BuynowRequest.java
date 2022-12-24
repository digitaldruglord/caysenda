package com.nomi.caysenda.controller.requests.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuynowRequest {
    String fullname;
    String phone;
    String address;
    String ward;
    String dictrict;
    String province;
    Integer productId;
    Integer variantId;
    String note;
}
