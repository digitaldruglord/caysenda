package com.nomi.caysenda.controller.responses.payment;

public class PaymentResponse {
    Boolean success;
    String message;
    String code;
    String fragment;

    public PaymentResponse() {
    }

    public PaymentResponse(Boolean success, String message, String code, String fragment) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.fragment = fragment;
    }

    public PaymentResponse(Boolean success, String message, String code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
