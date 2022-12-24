package com.nomi.caysenda.ghn.service;

import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.model.model.ShopModel;
import com.nomi.caysenda.ghn.model.request.CreateOrderModelRequest;
import com.nomi.caysenda.ghn.model.request.FeeModelRequest;
import com.nomi.caysenda.ghn.model.request.Webhook;
import com.nomi.caysenda.ghn.model.responses.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface GHNService {
    List<GHNOrderEntity> getOrders();
    GHNOrderEntity getByOrderId(Integer orderId);
    List<ShopModel> getShopByToken(String token);
    CreateOrderModelRequest getData(Integer orderId);
    List<GHNOrderEntity> findByIds(List<Integer> ids);
    List<GHNOrderEntity> getOrder(Integer order, Sort sort);
    OrderInfoModelResponse getDataFromGHN(String order_code);
    OrderInfoModelResponse getDataFromGHN(Integer orderId);
    PickShiftModelResponse getPickShift(String token);
    FeeModelResponse getFee(String token, FeeModelRequest modelRequest);
    ServiceModelResponse getService(String token,Integer to_district);
    CreateOrderModelResponse createOrder(String token,CreateOrderModelRequest modelRequest);
    UpdateModelResponse updateOrder(String token,CreateOrderModelRequest modelRequest);
    String print(String order_code);
    GHNBaseResponse cancel(String order_code);
    Boolean existsByOrderId(Integer orderId);
    Boolean existsByOrder_code(String orderCode);
    void webhook(Map webhook);
    String tracking(Integer orderId);
    List<OrderSummary> findAllByOrderId(Integer orderId);
}
