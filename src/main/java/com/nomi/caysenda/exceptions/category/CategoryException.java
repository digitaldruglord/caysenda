package com.nomi.caysenda.exceptions.category;

public class CategoryException extends Exception{
    public static final String SKU_SLUG_EXISTS = "SKU_SLUG_EXISTS";
    public static final String EMPTY_RESULT = "EMPTY_RESULT";
    String code;

    public CategoryException(String message, String code) {
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
