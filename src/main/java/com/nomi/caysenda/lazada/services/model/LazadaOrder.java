package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LazadaOrder {
    Long voucher;
    String warehouse_code;
    String order_number;
    String created_at;
    String voucher_code;
    Boolean gift_option;
    Double shipping_fee_discount_platform;
    String customer_last_name;
    String updated_at;
    String promised_shipping_times;
    Double price;
    String national_registration_number;
    Double shipping_fee_original;
    String payment_method;
    String customer_first_name;
    Double shipping_fee_discount_seller;
    Double shipping_fee;
    String branch_number;
    String tax_code;
    Integer items_count;
    String delivery_info;
    List<String> statuses;
    LazadaAddress address_billing;
    String order_id;
    LazadaAddress address_shipping;

}
