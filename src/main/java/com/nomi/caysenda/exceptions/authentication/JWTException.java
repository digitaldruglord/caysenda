package com.nomi.caysenda.exceptions.authentication;

public class JWTException extends Exception{
    public static final String USER_NOTFOUND= "USER_NOTFOUND";
    public static final String TOKEN_EXPIRE= "TOKEN_EXPIRE";
    public static final String TOKEN_MALFORMED= "TOKEN_MALFORMED";
    public static final String TOKEN_UNSUPPORT= "TOKEN_UNSUPPORT";
    public static final String TOKEN_ILLEAGLARGUMENT= "TOKEN_ILLEAGLARGUMENT";
    String code;

    public JWTException(String message) {
        super(message);
    }

    public JWTException(String message, String code) {
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
