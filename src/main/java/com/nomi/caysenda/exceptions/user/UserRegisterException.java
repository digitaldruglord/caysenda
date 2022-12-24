package com.nomi.caysenda.exceptions.user;

public class UserRegisterException extends Exception {
    public static final String USERNAME_EXIST_CODE = "USERNAME_EXIST";
    public static final String EMAIL_EXIST_CODE = "EMAIL_EXIST";
    public static final String USERNAME_EMAIL_EXIST_CODE = "ACCOUNT_EMAIL_EXIST";
    public static final String CONFIRM_PASSWORD_INCORECT_CODE = "CONFIRM_PASSWORD_INCORECT";
    public static final String USERNAME_EXIST_MESSAGE = "Account already exists";
    public static final String USERNAME_EMAIL_EXIST_MESSAGE = "Account or Email already exists";
    public static final String EMAIL_EXIST_MESSAGE = "Email already exists";
    public static final String PHONE_EXIST_CODE = "PHONE_EXIST_CODE";
    public static final String PHONE_EXIST_MESSAGE = "Phone already exists";
    public static final String CONFIRM_PASSWORD_INCORECT_MESSAGE = "Email already exists";
    public static final String MISSING_INFOMATION_CODE = "MISSING_INFOMATION_CODE";
    public static final String MISSING_INFOMATION_MESSAGE = "Missing information";
    private String code;

    public UserRegisterException(String message, String code) {
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
