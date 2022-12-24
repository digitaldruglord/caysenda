package com.nomi.caysenda.ghn.api;

import com.nomi.caysenda.ghn.model.request.CreateOrderModelRequest;
import com.nomi.caysenda.ghn.model.request.FeeModelRequest;
import com.nomi.caysenda.ghn.model.request.ServiceModelRequset;
import com.nomi.caysenda.ghn.model.request.StoreModelRequest;
import com.nomi.caysenda.ghn.model.responses.*;
import org.springframework.http.HttpHeaders;

public interface GHN {
    HttpHeaders getHeaders();
    ServiceModelResponse getService(String token,ServiceModelRequset requset);
    StoreModelResponse getStore(StoreModelRequest request,String token);
    PickShiftModelResponse getPickShift(String token);
    FeeModelResponse getFee(String token, FeeModelRequest modelRequest);
    CreateOrderModelResponse createOrder(String token, CreateOrderModelRequest modelRequest);
    UpdateModelResponse updateOrder(String token, CreateOrderModelRequest modelRequest);
    OrderInfoModelResponse getOrder(String token,String order_code);
    String print(String token,String order_code);
    GHNBaseResponse cancel(String token,String order_code);
}
