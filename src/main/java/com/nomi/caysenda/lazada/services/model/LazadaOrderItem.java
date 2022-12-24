package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaOrderItem {
    String name;
    String variation;
    String product_id;
    String sku;
    String shop_sku;
    String order_id;
    String status;
    String paid_price;
    String shipping_type;
    String created_at;
    String updated_at;
    String item_price;
    String shipping_amount;


}
