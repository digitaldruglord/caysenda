package com.nomi.caysenda.lazada.services;

import com.nomi.caysenda.lazada.services.model.LazadaOrderItemResponse;
import com.nomi.caysenda.lazada.services.model.LazadaOrderResponse;
import com.nomi.caysenda.lazada.util.ApiException;

public interface LazadaOrderService {
    void getOrders() throws ApiException;
    LazadaOrderResponse getOrderById(String id) throws ApiException;
    LazadaOrderItemResponse getOrderItems(String id) throws ApiException;
    void setInvoiceNumber(String id) throws ApiException;
    void setRepack(String id) throws ApiException;
    void setStatusToCanceled(String id) throws ApiException;
    void setStatusToPackedByMarketplace(String id) throws ApiException;
    void setStatusToReadyToShip(String id) throws ApiException;
    void setStatusToSOFDelivered(String id) throws ApiException;
    void setStatusToSOFFailedDelivery(String id) throws ApiException;
    void create(String id) throws ApiException;
}
