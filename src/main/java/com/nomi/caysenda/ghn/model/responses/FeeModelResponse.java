package com.nomi.caysenda.ghn.model.responses;

public class FeeModelResponse extends GHNBaseResponse {
    DataFeeModelResponse data;

    public DataFeeModelResponse getData() {
        return data;
    }

    public void setData(DataFeeModelResponse data) {
        this.data = data;
    }
}
