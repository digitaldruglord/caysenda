package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaOrderResponse {
    String code;
    String request_id;
    LazadaOrder data;

}
