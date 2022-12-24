package com.nomi.caysenda.controller.responses.address;

public class AddressResponse {
    Boolean success;
    String message;
    String redirectTo;

    public AddressResponse() {
    }

    public AddressResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AddressResponse(Boolean success, String message, String redirectTo) {
        this.success = success;
        this.message = message;
        this.redirectTo = redirectTo;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
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
