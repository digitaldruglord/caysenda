package com.nomi.caysenda.ghn.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeeModelRequest {
    Integer from_district_id;
    Integer service_id;
    Integer service_type_id;
    Integer to_district_id;
    String to_ward_code;
    Integer height;
    Integer length;
    Integer weight;
    Integer width;
    Integer insurance_fee;
    String coupon;

}
