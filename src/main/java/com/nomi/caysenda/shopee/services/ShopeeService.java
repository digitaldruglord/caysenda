package com.nomi.caysenda.shopee.services;

import com.nomi.caysenda.shopee.entities.ShopeeCategoryEntity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ShopeeService {
    void generateShopeeExcel(Integer catId,Integer shopeeCat) throws IOException, InvalidFormatException;
    void importShopeeCategory() throws IOException;
    Page<ShopeeCategoryEntity> findALlByName(String name, Pageable pageable);
}
