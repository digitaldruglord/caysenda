package com.nomi.caysenda.ghtk.models;

public class OrderResponse {
    boolean success;
    String message;
    OrderCreateResponse order;
    ErrorCreateResponse error;

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

    public OrderCreateResponse getOrder() {
        return order;
    }

    public void setOrder(OrderCreateResponse order) {
        this.order = order;
    }

    public ErrorCreateResponse getError() {
        return error;
    }

    public void setError(ErrorCreateResponse error) {
        this.error = error;
    }



}
