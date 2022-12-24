package com.nomi.caysenda.exceptions.product;

public class ProductException extends Exception {
    /***/
    public static final String EXISTS_SKUVI ="EXISTS_SKUVI";
    public static final String EXISTS_SKUZH ="EXISTS_SKUZH";
    public static final String EXISTS_SLUG ="EXISTS_SLUG";
    public static final String EXISTS_VARIANT_SKU ="EXISTS_VARIANT_SKU";
    public static final String DATA_EXCEPTION ="DataIntegrityViolationException";
    /***/
    private String code;

    public ProductException(String code) {
        this.code = code;
    }

    public ProductException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
