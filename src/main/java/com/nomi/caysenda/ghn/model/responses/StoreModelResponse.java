package com.nomi.caysenda.ghn.model.responses;

public class StoreModelResponse extends GHNBaseResponse{
    DataStoreModelResponse data;

    public DataStoreModelResponse getData() {
        return data;
    }

    public void setData(DataStoreModelResponse data) {
        this.data = data;
    }
}
