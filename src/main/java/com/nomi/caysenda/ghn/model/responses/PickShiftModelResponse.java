package com.nomi.caysenda.ghn.model.responses;

import java.util.List;

public class PickShiftModelResponse extends GHNBaseResponse{
    List<DataPickShiftModelResponse> data;

    public List<DataPickShiftModelResponse> getData() {
        return data;
    }

    public void setData(List<DataPickShiftModelResponse> data) {
        this.data = data;
    }
}
