package com.nomi.caysenda.exceptions.cart;

public class AddToCartException extends Exception{
    public static String NOT_ENOUGH_STOCK_CODE = "NOT_ENOUGH_STOCK";
    public static String NOT_ENOUGH_STOCK_MESSAGE = "Not enough stock";
    public static String ENOUGH_CONDITION = "ENOUGH_CONDITION";
    public static String ENOUGH_CONDITION_MESSAGE = "ENOUGH_CONDITION_MESSAGE";
    public static String LOGIN = "LOGIN";
    public static String LOGIN_MESSAGE = "please login your account";
    String code;
    Integer quantity;
    public AddToCartException(String message) {
        super(message);
    }

    public AddToCartException(String message, String code) {
        super(message);
        this.code = code;
    }

    public AddToCartException(String message, String code, Integer quantity) {
        super(message);
        this.code = code;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
