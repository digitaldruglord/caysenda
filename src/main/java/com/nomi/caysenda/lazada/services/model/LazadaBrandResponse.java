package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaBrandResponse {
    LazadaBrand data;
    Boolean success;
    String code;
    String request_id;

}
