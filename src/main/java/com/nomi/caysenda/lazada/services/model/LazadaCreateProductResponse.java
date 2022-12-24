package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaCreateProductResponse {
    LazadaCreateProduct data;
    Integer code;
    String request_id;

}
