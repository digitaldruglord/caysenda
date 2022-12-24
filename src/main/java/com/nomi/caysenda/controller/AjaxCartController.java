package com.nomi.caysenda.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.controller.responses.payment.PaymentResponse;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface AjaxCartController {
    @PostMapping("/addtocart")
    ResponseEntity<CartRequestResponese> addtocart(@RequestBody CartRequest request) throws AddToCartException;
    @GetMapping("/update")
    ResponseEntity<CartRequestResponese> updateCart(@RequestParam("productId") Long productId,
                                                    @RequestParam("variantId") Long variantId,
                                                    @RequestParam("quantity") Integer quantity) throws AddToCartException;
    @GetMapping("/delete")
    ResponseEntity<CartRequestResponese> delete(@RequestParam("productId") List<Long> productId);
    @GetMapping("/active")
    ResponseEntity<CartRequestResponese> active(@RequestParam(value = "catId",required = false) Integer catId,
                                                @RequestParam(value = "productId",required = false) Long productId,
                                                @RequestParam(value = "variantId",required = false) Long variantId,
                                                @RequestParam("active") Boolean active);
    @GetMapping("/confirm")
    ResponseEntity<PaymentResponse> confirm(@RequestParam("method") String method,
                                            @RequestParam(value = "note",required = false) String note);
    @GetMapping("/update-widget")
    ResponseEntity<Map> updateWidget();
    @GetMapping("/get-fee-delivery")
    ResponseEntity<Map> getFeeDelivery();
    @GetMapping("/quickview")
    ResponseEntity<Map> getQuickview(@RequestParam("id") Integer id) throws JsonProcessingException;
}
