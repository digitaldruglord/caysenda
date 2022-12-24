package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LazadaCreateProduct {
    String item_id;
    List<LazadaSkuList> sku_list;

}
