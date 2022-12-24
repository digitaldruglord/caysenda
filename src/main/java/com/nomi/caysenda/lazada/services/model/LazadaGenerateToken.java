package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaGenerateToken {
    String access_token;
    String country;
    String refresh_token;
    Long expires_in;
    String account;
    String code;
    String type;
    String message;
    String request_id;


}
