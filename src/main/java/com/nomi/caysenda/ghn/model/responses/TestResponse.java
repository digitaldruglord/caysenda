package com.nomi.caysenda.ghn.model.responses;

public class TestResponse<T> extends GHNBaseResponse{
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
