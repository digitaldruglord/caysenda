package com.nomi.caysenda.services.delivery;

import com.nomi.caysenda.api.admin.delivery.model.*;
import com.nomi.caysenda.api.web.models.ChangePasswordRequest;
import com.nomi.caysenda.api.web.models.ContactFormRequest;
import com.nomi.caysenda.api.web.models.UserDeliveryRegister;
import com.nomi.caysenda.entity.ContactFormDeliveryEntity;
import com.nomi.caysenda.entity.delivery.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DeliveryService {
    UserDeliveryEntity getUserDelivery(String token);
    Map register(UserDeliveryRegister deliveryRegister);
    Map login(String userName,String password);
    Map changePassword(ChangePasswordRequest request);
    ContactFormDeliveryEntity createForm(ContactFormRequest formRequest);
    List<ContactFormDeliveryEntity> findAllByUserAndType();
    Map getStatus();
    /** order */
    DeliveryOrderEntity findOrderById(Integer id);
    DeliveryOrderEntity findOrderById(String token,Integer id);
    Page<DeliveryOrderEntity> findAllOrder(Pageable pageable);
    List<DeliveryOrderEntity> getDeliveryOrder(String token);
    DeliveryOrderEntity createOrder(ContactFormRequest formRequest);
    DeliveryOrderEntity updateOrder(UpdateDeliveryOrderRequest map);
    DeliveryOrderStatus addDeliveryStatus(AddDeliveryOrderStatusRequest request);
    Long deliveryOrderCount();

    /** currency */
    DeliveryCurrencyEntity createCurrency(ContactFormRequest formRequest);
    Page<DeliveryCurrencyEntity> findAllCurrency(String token,Pageable pageable);
    Page<DeliveryCurrencyEntity> findAllCurrency(Pageable pageable);
    DeliveryCurrencyEntity findCurrencyById(String token,Integer id);
    DeliveryCurrencyEntity updateCurrency(UpdateDeliveryCurrencyRequest request);
    DeliveryCurrencyEntity updateStatus(ChangeStatusCurrencyRequest request);
    Long currencyCount();

    /** delivery */
    DeliveryEntity findDeliveryById(Integer id);
    DeliveryEntity findDeliveryById(String token, Integer id);
    Page<DeliveryEntity> findAllDelivery(Pageable pageable);
    Page<DeliveryEntity> getDelivery(String token,Pageable pageable);
    DeliveryEntity createDelivery(ContactFormRequest formRequest);
    DeliveryEntity updateDelivery(UpdateDeliveryRequest map);
    DeliveryStatusEntity addDeliveryStatus(AddDeliveryStatusRequest request);
    Long deliveryCount();

}
