package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LazadaCategorySuggestionResponse {
    String code;
    LazadaCategorySuggetions data;
    String request_id;

}
