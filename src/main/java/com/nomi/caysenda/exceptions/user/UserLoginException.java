package com.nomi.caysenda.exceptions.user;

public class UserLoginException extends Exception{
    public static final Boolean STAUS_SUCCESS = true;
    public static final Boolean STAUS_FAIL = false;
    public static final String MESSAGE_SUCCESS = "Login success";
    public static final String MESSAGE_FAIL = "Username or password incorect";
    public static final String MESSAGE_NOT_FOUND = "User not found";
    public static final String CODE_NOT_FOUND = "NOT_FOUNT";
    public static final String CODE_INCORECT = "INCORECT";
    Boolean status;
    String code;

    public UserLoginException(String message) {
        super(message);
    }

    public UserLoginException(String message, Boolean status) {
        super(message);
        this.status = status;
    }

    public UserLoginException(String message, Boolean status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
