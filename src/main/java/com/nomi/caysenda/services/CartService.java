package com.nomi.caysenda.services;

import com.nomi.caysenda.api.admin.model.order.request.AdminOrderSplitRequest;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.dto.cart.CartSummary;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CartService {
    CartRequestResponese addtocart(CartRequest request) throws AddToCartException;
    Boolean checkCondition(Long total,Integer condition) throws AddToCartException;
    CartDTO getCart();
    CartDTO getCart(Integer id);
    Map summaryCart(CartDTO cartDTO);
    Long countByProduct(Integer productId);
    CartRequestResponese update(Long productId,Long variantId,Integer quantity) throws AddToCartException;
    CartRequestResponese activeAll(Boolean active);
    CartRequestResponese activeByCat(Integer cat,Boolean active);
    CartRequestResponese activeByProduct(Integer catId,Long product,Boolean active);
    CartRequestResponese activeByVariant(Integer catId,Long product,Long variant,Boolean active);
    CartRequestResponese removeByProduct(Long product);
    CartRequestResponese removeByProduct(List<Long> products);
    CartRequestResponese removeAllActive();
    Map getDataWidget();
    Map getDelivery();
    void login(String sessionId);
    Boolean checkPayment();
    Page<CartSummary> statictisCart(String sort,String keyword,Pageable pageable);
    CartSummary statictisCart(Integer id);
    void updateCartNote(Integer id,String note);
    void updateModified(Integer id);
}
