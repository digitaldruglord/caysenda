package com.nomi.caysenda.ghn.model.responses;

public class CreateOrderModelResponse extends GHNBaseResponse {
    DataCreateOrderModalResponse data;
    public DataCreateOrderModalResponse getData() {
        return data;
    }

    public void setData(DataCreateOrderModalResponse data) {
        this.data = data;
    }
}
