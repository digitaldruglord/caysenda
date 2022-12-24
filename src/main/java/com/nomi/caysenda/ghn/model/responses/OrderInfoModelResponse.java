package com.nomi.caysenda.ghn.model.responses;

public class OrderInfoModelResponse extends GHNBaseResponse{
    DataOrderInfoResponse data;
    String token;
    public DataOrderInfoResponse getData() {
        return data;
    }

    public void setData(DataOrderInfoResponse data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
