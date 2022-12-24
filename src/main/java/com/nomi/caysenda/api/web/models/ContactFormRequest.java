package com.nomi.caysenda.api.web.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class ContactFormRequest {
    String type;
    String token;
    Map data;
}
