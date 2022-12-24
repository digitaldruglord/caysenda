package com.nomi.caysenda.ghn.model.responses;

import java.util.List;

public class ServiceModelResponse extends GHNBaseResponse{
    List<DataServiceResponse> data;

    public List<DataServiceResponse> getData() {
        return data;
    }

    public void setData(List<DataServiceResponse> data) {
        this.data = data;
    }
}
