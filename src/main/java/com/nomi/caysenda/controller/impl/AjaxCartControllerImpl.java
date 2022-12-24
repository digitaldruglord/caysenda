package com.nomi.caysenda.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nomi.caysenda.controller.AjaxCartController;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.controller.responses.payment.PaymentResponse;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import com.nomi.caysenda.services.CartService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ajax/cart")
public class AjaxCartControllerImpl implements AjaxCartController {
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    ProductService productService;
    @Override
    public ResponseEntity<CartRequestResponese> addtocart(CartRequest request) throws AddToCartException {
        return ResponseEntity.ok(cartService.addtocart(request));
    }

    @Override
    public ResponseEntity<CartRequestResponese> updateCart(Long productId, Long variantId, Integer quantity) throws AddToCartException {
        return ResponseEntity.ok(cartService.update(productId,variantId,quantity));
    }

    @Override
    public ResponseEntity<CartRequestResponese> delete(List<Long> productId) {
        return ResponseEntity.ok(cartService.removeByProduct(productId));
    }

    @Override
    public ResponseEntity<CartRequestResponese> active(Integer catId, Long productId, Long variantId, Boolean active) {
        CartRequestResponese cartRequestResponese = new CartRequestResponese(false,"ádasd");
        if (catId!=null && productId==null && variantId==null){
            cartRequestResponese = cartService.activeByCat(catId,active);
        }else if (catId!=null && productId!=null && variantId==null){
            cartRequestResponese = cartService.activeByProduct(catId,productId,active);
        }else if (catId!=null && productId!=null && variantId!=null){
            cartRequestResponese =  cartService.activeByVariant(catId,productId,variantId, active);
        }else {
            cartRequestResponese = cartService.activeAll(active);
        }

        return ResponseEntity.ok(cartRequestResponese);
    }

    @Override
    public ResponseEntity<PaymentResponse> confirm(String method, String note) {
        PaymentResponse paymentResponse = orderService.confirm(method,note);
        return ResponseEntity.ok(paymentResponse);
    }

    @Override
    public ResponseEntity<Map> updateWidget() {
        Map map = new HashMap();
        map.putAll(cartService.getDataWidget());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getFeeDelivery() {
        return ResponseEntity.ok(cartService.getDelivery());
    }

    @Override
    public ResponseEntity<Map> getQuickview(Integer id) throws JsonProcessingException {
        return ResponseEntity.ok(Map.of("success",true,"data",productService.getQuickview(id)));
    }
}
