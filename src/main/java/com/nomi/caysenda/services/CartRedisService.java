package com.nomi.caysenda.services;

import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.exceptions.cart.AddToCartException;

public interface CartRedisService {
    CartRequestResponese addtocart(CartRequest request) throws AddToCartException;
    Boolean checkCondition(Long total,Integer condition) throws AddToCartException;
    CartDTO getCart();
    Long countByProduct(Integer productId);
    CartRequestResponese update(Long productId,Long variantId,Integer quantity) throws AddToCartException;
    CartRequestResponese activeAll(Boolean active);
    CartRequestResponese activeByCat(Integer cat,Boolean active);
    CartRequestResponese activeByProduct(Integer catId,Long product,Boolean active);
    CartRequestResponese activeByVariant(Integer catId,Long product,Long variant,Boolean active);
    CartRequestResponese removeByProduct(Long product);
    CartRequestResponese removeAllActive();
    Boolean checkPayment();
}
