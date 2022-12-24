package com.nomi.caysenda.lazada.services;

import com.nomi.caysenda.lazada.entity.LazadaProductEntity;
import com.nomi.caysenda.lazada.services.model.LazadaBrandResponse;
import com.nomi.caysenda.lazada.services.model.LazadaCategoryAttributeResponse;
import com.nomi.caysenda.lazada.services.model.LazadaCategoryResponse;
import com.nomi.caysenda.lazada.services.model.LazadaCategorySuggestionResponse;
import com.nomi.caysenda.lazada.util.ApiException;

import java.util.List;

public interface LazadaProductService {
    LazadaProductEntity createByProductId(Integer productId,Integer categoryId, Integer lazadaCategoryId) throws Exception;
    void createByCategoryId(Integer categoryId, Integer lazadaCategoryId) throws Exception;
    LazadaCategoryResponse getCategoryTree() throws ApiException;
    LazadaBrandResponse getBrandByPages(Integer page,Integer pageSize) throws ApiException;
    LazadaCategoryAttributeResponse getCategoryAttributes(Integer categoryId) throws ApiException;
    LazadaCategorySuggestionResponse getCategorySuggestion() throws ApiException;
    void updateProduct();
    void removeByProductSku(String sku) throws ApiException;
    void removeBySku(String id) throws ApiException;
    void removeBySku(List<String> skus) throws ApiException;
    void removeProductByCatId(Integer categoryId) throws ApiException;
}
