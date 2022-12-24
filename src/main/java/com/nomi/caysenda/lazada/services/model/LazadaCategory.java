package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LazadaCategory {
    Boolean var;
    String name;
    Boolean leaf;
    Integer category_id;
    List<LazadaCategory> children;

}
