package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LazadaCategoryAttributeResponse {
    List<LazadaCategoryAttribute> data;
    String code;
    String request_id;

}
