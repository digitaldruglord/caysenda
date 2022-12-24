package com.nomi.caysenda.lazada.services.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LazadaBrand {
    Boolean enable_total;
    Integer start_row;
    Integer page_index;
    List<LazadaBrandModule> module;
    Integer total_page;
    Integer page_size;
    Integer total_record;

}
