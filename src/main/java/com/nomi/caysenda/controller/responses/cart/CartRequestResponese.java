package com.nomi.caysenda.controller.responses.cart;

import com.nomi.caysenda.dto.cart.CartCategoryDTO;
import com.nomi.caysenda.dto.cart.CartProductDTO;

import java.util.List;

public class CartRequestResponese {
    boolean success;
    String code;
    String message;
    List<CartProductDTO> cartProductList;
    Boolean activeProduct;
    Boolean activeCategory;
    Long amountCategory;

    public CartRequestResponese() {
    }

    public CartRequestResponese(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CartRequestResponese(boolean success, String message, Boolean activeCategory) {
        this.success = success;
        this.message = message;
        this.activeCategory = activeCategory;
    }

    public CartRequestResponese(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public CartRequestResponese(boolean success, String message, List<CartProductDTO> cartProductList) {
        this.success = success;
        this.message = message;
        this.cartProductList = cartProductList;
    }

    public CartRequestResponese(boolean success, String message, List<CartProductDTO> cartProductList, Boolean activeCategory) {
        this.success = success;
        this.message = message;
        this.cartProductList = cartProductList;
        this.activeCategory = activeCategory;
    }

    public CartRequestResponese(boolean success, String message, List<CartProductDTO> cartProductList, Boolean activeProduct, Long amountCategory) {
        this.success = success;
        this.message = message;
        this.cartProductList = cartProductList;
        this.activeProduct = activeProduct;
        this.amountCategory = amountCategory;
    }

    public Long getAmountCategory() {
        return amountCategory;
    }

    public void setAmountCategory(Long amountCategory) {
        this.amountCategory = amountCategory;
    }

    public Boolean getActiveProduct() {
        return activeProduct;
    }

    public void setActiveProduct(Boolean activeProduct) {
        this.activeProduct = activeProduct;
    }

    public Boolean getActiveCategory() {
        return activeCategory;
    }

    public void setActiveCategory(Boolean activeCategory) {
        this.activeCategory = activeCategory;
    }

    public List<CartProductDTO> getCartProductList() {
        return cartProductList;
    }

    public void setCartProductList(List<CartProductDTO> cartProductList) {
        this.cartProductList = cartProductList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
