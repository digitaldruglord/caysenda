package com.nomi.caysenda.controller.responses;

public class UserRegisterResponse {
    Boolean success;
    String message;

    public UserRegisterResponse() {
    }

    public UserRegisterResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
