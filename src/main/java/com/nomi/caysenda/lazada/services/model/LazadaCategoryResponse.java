package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LazadaCategoryResponse {
    List<LazadaCategory> data;
    String code;
    String request_id;

}
