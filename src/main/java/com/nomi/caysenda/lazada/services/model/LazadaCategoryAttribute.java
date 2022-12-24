package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class LazadaCategoryAttribute {
    Map advanced;
    Integer is_sale_prop;
    String name;
    String input_type;
    List<Map> options;
    Integer is_mandatory;
    String attribute_type;
    String label;
    Long id;

}
