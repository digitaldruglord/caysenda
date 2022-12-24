package com.nomi.caysenda.ghn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.ghn.api.GHN;
import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.entity.GHNOrderItemEntity;
import com.nomi.caysenda.ghn.model.model.ShopModel;
import com.nomi.caysenda.ghn.model.request.*;
import com.nomi.caysenda.ghn.model.responses.*;
import com.nomi.caysenda.ghn.model.setting.GHNSetttingModel;
import com.nomi.caysenda.ghn.model.setting.GHNTokenModel;
import com.nomi.caysenda.ghn.repositories.GhnOrderRepository;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.ghn.service.GHNSettingService;
import com.nomi.caysenda.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GHNServiceImpl implements GHNService {
    @Autowired
    GHN ghn;
    @Autowired
    OrderService orderService;
    @Autowired
    GHNSettingService ghnSettingService;
    @Autowired
    GhnOrderRepository ghnOrderRepository;

    @Override
    public List<GHNOrderEntity> getOrders() {
        return ghnOrderRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public GHNOrderEntity getByOrderId(Integer orderId) {

        return null;
    }

    @Override
    public List<ShopModel> getShopByToken(String token) {
        StoreModelResponse response = ghn.getStore(new StoreModelRequest(), token);
        if (response == null) return new ArrayList<>();
        if (response.getCode().equals(200)) {
            return response.getData().getShops();
        }
        return new ArrayList<>();
    }

    @Override
    public CreateOrderModelRequest getData(Integer orderId) {
        OrderEntity orderEntity = orderService.findById(orderId);
        List<OrderDetailtEntity> detailts = orderService.findAllDetailtByOrderId(orderId);
        CreateOrderModelRequest modelRequest = new CreateOrderModelRequest();
        modelRequest.setOrderId(orderId);
        Random random = new Random();
        String randomID = "NOMI_" + orderEntity.getId() + "_" + random.nextInt(100000);
        while (ghnOrderRepository.existsByOrderCode(randomID)) {
            randomID = "NOMI_" + orderEntity.getId() + "_" + random.nextInt(100000);
        }
        modelRequest.setClient_order_code(randomID);
        modelRequest.setNote(orderEntity.getNote());
        modelRequest.setTo_name(orderEntity.getBillingFullName());
        modelRequest.setTo_phone(orderEntity.getBillingPhoneNumber());
        modelRequest.setTo_address(orderService.getFullAddress(orderEntity.getBillingAddress(), orderEntity.getBillingCity(), orderEntity.getBillingDistrict(), orderEntity.getBillingWards()));
        modelRequest.setTo_province(orderEntity.getBillingCity());
        modelRequest.setTo_ward_code(orderEntity.getBillingWards());
        modelRequest.setTo_district_id(Integer.valueOf(orderEntity.getBillingDistrict()));
        modelRequest.setCod_amount(orderEntity.getOrderAmount() - orderEntity.getPaid());
        Integer weight = detailts.stream().mapToInt(orderDetailtEntity -> orderDetailtEntity.getWeight().intValue() * orderDetailtEntity.getQuantity()).sum();
        modelRequest.setWeight(weight > 0 ? weight : 5000);
        modelRequest.setInsurance_value(orderEntity.getProductAmount());
        modelRequest.setWidth(Double.valueOf(10).intValue());
        modelRequest.setHeight(Double.valueOf(10).intValue());
        modelRequest.setLength(Double.valueOf(10).intValue());
        modelRequest.setRequired_note("CHOTHUHANG");
        modelRequest.setPayment_type_id(1);
        modelRequest.setFee(Long.valueOf(0));
        modelRequest.setInsurance_fee(Long.valueOf(0));
        modelRequest.setItems(List.of(new GHNOrderItemEntity("Phụ kiện chậu trồng cây", "PKCTC", detailts.stream().mapToInt(orderDetailtEntity -> orderDetailtEntity.getQuantity()).sum())));
        return modelRequest;
    }

    @Override
    public List<GHNOrderEntity> findByIds(List<Integer> ids) {

        return ghnOrderRepository.findAllByOrderId(ids);
    }

    @Override
    public List<GHNOrderEntity> getOrder(Integer order, Sort sort) {
        return ghnOrderRepository.findAllByOrderId(order, sort);
    }

    @Override
    public OrderInfoModelResponse getDataFromGHN(String order_code) {
        GHNOrderEntity ghnOrderEntity = ghnOrderRepository.findByOrderCodeGhn(order_code);
        if (ghnOrderEntity != null) {
            OrderInfoModelResponse response = ghn.getOrder(ghnOrderEntity.getToken(), order_code);
            response.getData().setTo_province(ghnOrderEntity.getTo_province());
            response.setToken(ghnOrderEntity.getToken());
            response.getData().setFee(ghnOrderEntity.getMain_service());
            response.getData().setInsurance_fee(ghnOrderEntity.getInsurance_fee());
            response.getData().setClient_order_code(ghnOrderEntity.getOrderCode());
            response.getData().setOrderId(ghnOrderEntity.getOrderId());
            return response;
        }
        return null;
    }

    @Override
    public OrderInfoModelResponse getDataFromGHN(Integer orderId) {
        List<GHNOrderEntity> orders = ghnOrderRepository.findAllByOrderId(orderId, Sort.by(Sort.Direction.DESC, "id"));
        if (orders.size() > 0) {
            OrderInfoModelResponse response = ghn.getOrder(orders.get(0).getToken(), orders.get(0).getOrderCodeGhn());
            response.getData().setTo_province(orders.get(0).getTo_province());
            response.setToken(orders.get(0).getToken());
            response.getData().setFee(orders.get(0).getMain_service());
            response.getData().setInsurance_fee(orders.get(0).getInsurance_fee());
            response.getData().setClient_order_code(orders.get(0).getOrderCode());
            response.getData().setOrderId(orders.get(0).getOrderId());
            return response;
        }
        return null;
    }

    @Override
    public PickShiftModelResponse getPickShift(String token) {
        return ghn.getPickShift(token);
    }

    @Override
    public FeeModelResponse getFee(String token, FeeModelRequest modelRequest) {
        ShopModel shopModel = getDataShop(token);
        if (shopModel != null) {
            modelRequest.setFrom_district_id(shopModel.getDistrict_id());
        }
        if (modelRequest.getWeight().equals(0)) modelRequest.setWeight(200);
        FeeModelResponse response = ghn.getFee(token, modelRequest);
        return response;
    }

    @Override
    public ServiceModelResponse getService(String token, Integer to_district) {
        ShopModel shopModel = getDataShop(token);
        ServiceModelRequset serviceModelRequset = new ServiceModelRequset(shopModel.get_id(), shopModel.getDistrict_id(), to_district);
        return ghn.getService(token, serviceModelRequset);
    }

    @Override
    public CreateOrderModelResponse createOrder(String token, CreateOrderModelRequest modelRequest) {

        CreateOrderModelResponse orderModelResponse = ghn.createOrder(token, modelRequest);
        if (orderModelResponse.getCode().equals(200)) {
            GHNOrderEntity ghnOrderEntity = new GHNOrderEntity();
            ghnOrderEntity.setOrderId(modelRequest.getOrderId());
            ghnOrderEntity.setOrderCodeGhn(orderModelResponse.getData().getOrder_code());
            ghnOrderEntity.setStatus("created");
            ghnOrderEntity.setOrderCode(modelRequest.getClient_order_code());
            ghnOrderEntity.setSortCode(orderModelResponse.getData().getSort_code());
            ghnOrderEntity.setToken(token);
            ghnOrderEntity.setName(modelRequest.getTo_name());
            ghnOrderEntity.setPhoneNumber(modelRequest.getTo_phone());
            ghnOrderEntity.setAmount(modelRequest.getInsurance_value());
            ghnOrderEntity.setCod(modelRequest.getCod_amount());
            ghnOrderEntity.setTo_province(modelRequest.getTo_province());
            DataFeeCreateOrderModelResponse fee = orderModelResponse.getData().getFee();
            ghnOrderEntity.setMain_service(fee.getMain_service());
            ghnOrderEntity.setInsurance_fee(fee.getInsurance());
            ghnOrderEntity.setTotalFee(orderModelResponse.getData().getTotal_fee());
            String productName = "";
            if (modelRequest.getItems().size()>0){
                productName = modelRequest.getItems().get(0).getName();
            }
            ghnOrderEntity.setProductName(productName);
            ghnOrderRepository.save(ghnOrderEntity);

        }

        return orderModelResponse;
    }

    @Override
    public UpdateModelResponse updateOrder(String token, CreateOrderModelRequest modelRequest) {
        GHNOrderEntity ghnOrderEntity = ghnOrderRepository.findByOrderCodeGhn(modelRequest.getOrder_code());
        modelRequest.setOrder_code(ghnOrderEntity.getOrderCodeGhn());
        UpdateModelResponse response = ghn.updateOrder(token, modelRequest);
        if (response.getCode().equals(200)) {
            ghnOrderEntity.setTo_province(modelRequest.getTo_province());
            ghnOrderEntity.setName(modelRequest.getTo_name());
            ghnOrderEntity.setPhoneNumber(modelRequest.getTo_phone());
            ghnOrderEntity.setAmount(modelRequest.getInsurance_value());
            ghnOrderEntity.setCod(modelRequest.getCod_amount());
            ghnOrderEntity.setTo_province(modelRequest.getTo_province());
            String productName = "";
            if (modelRequest.getItems().size()>0){
                productName = modelRequest.getItems().get(0).getName();
            }
            ghnOrderEntity.setProductName(productName);
            ghnOrderRepository.save(ghnOrderEntity);
        }
        response.setType("update");
        response.setMessage("Cập nhật thành công");
        return response;
    }

    @Override
    public String print(String order_code) {
        GHNOrderEntity ghnOrderEntity = ghnOrderRepository.findByOrderCodeGhn(order_code);
        return ghn.print(ghnOrderEntity.getToken(), ghnOrderEntity.getOrderCodeGhn());
    }

    @Override
    public GHNBaseResponse cancel(String order_code) {
        GHNOrderEntity ghnOrderEntity = ghnOrderRepository.findByOrderCodeGhn(order_code);
        GHNBaseResponse ghnBaseResponse = ghn.cancel(ghnOrderEntity.getToken(), order_code);
        if (ghnBaseResponse != null && ghnBaseResponse.getCode().equals(200)) {
            ghnOrderEntity.setStatus("cancel");
            ghnOrderRepository.delete(ghnOrderEntity);

        }
        return ghnBaseResponse;
    }

    @Override
    public Boolean existsByOrderId(Integer orderId) {
        return ghnOrderRepository.existsByOrderId(orderId);
    }

    @Override
    public Boolean existsByOrder_code(String orderCode) {
        return ghnOrderRepository.existsByOrderCodeGhn(orderCode);
    }

    @Override
    public void webhook(Map webhook) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(webhook));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String orderCode = String.valueOf(webhook.get("OrderCode"));
        String status = String.valueOf(webhook.get("Status"));
        String date = String.valueOf(webhook.get("Time"));
        String codString = String.valueOf(webhook.get("CODAmount"));
        GHNOrderEntity ghnOrderEntity = ghnOrderRepository.findByOrderCodeGhn(orderCode);
        if (ghnOrderEntity != null) {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date dateParse = dt.parse(date);
                ghnOrderEntity.setModifiedDate(dateParse);
            } catch (ParseException e) {

            }
            if (List.of("delivering","delivered","returned","damage","lost","picked","delivery_fail").contains(status)){
                ghnOrderEntity.setStatus(status);
                if (status.equals("returned") || status.equals("delivered")) {
                    ghnOrderRepository.save(ghnOrderEntity);
                    List<GHNOrderEntity> ghnOrderEntities = ghnOrderRepository.findAllByOrderId(ghnOrderEntity.getOrderId(), Sort.by(Sort.Direction.DESC, "id"));
                    Boolean success = true;
                    for (GHNOrderEntity entity : ghnOrderEntities) {
                        if (!entity.getStatus().equals("returned") && !entity.getStatus().equals("delivered")){
                            success = false;
                            break;
                        }
                    }
                    if (success){
                        OrderEntity orderEntity = orderService.findById(ghnOrderEntity.getOrderId());
                        if (!orderEntity.getCashflowstatus().equals("awaitingadditionaldelivery")){
                            orderEntity.setStatus("customerreceived");
                        }else {
                            orderEntity.setStatus("success");
                        }
                        orderService.save(orderEntity);

                    }
                } else if (status.equals("cancel")) {
                    ghnOrderRepository.delete(ghnOrderEntity);
                } else {
                    ghnOrderRepository.save(ghnOrderEntity);
                }
            }else {
                ghnOrderRepository.save(ghnOrderEntity);
            }

        }
    }

    @Override
    public String tracking(Integer orderId) {
        String baseUrlTracking = "https://donhang.ghn.vn/?order_code=";
        List<GHNOrderEntity> orders = getOrder(orderId, Sort.by(Sort.Direction.DESC, "id"));
        if (orders.size() > 0) {
            GHNOrderEntity ghnOrderEntity = orders.get(0);
            return baseUrlTracking + ghnOrderEntity.getOrderCodeGhn();
        }
        return null;
    }

    @Override
    public List<OrderSummary> findAllByOrderId(Integer orderId) {
        List<OrderSummary> orderSummaries = ghnOrderRepository.findAllByOrderId(orderId);
        orderSummaries.forEach(orderSummary -> {
            orderSummary.setStatusName(getStatusName(orderSummary.getStatus()));
            orderSummary.setColor(getColor(orderSummary.getStatus()));
        });
        return orderSummaries;
    }
    private String getStatusName(String status){
        switch (status){
            case "delivering": return "Đang vận chuyển" ;
            case "delivered": return "Đã giao hàng " ;
            case "returned": return "Đã hoàn thành" ;
            case "damage": return "Hỏng hàng" ;
            case "lost": return "Mất hàng" ;
            case "delivery_fail": return "Giao hàng thất bại" ;
            default:return "Đã tạo";
        }

    }
    private String getColor(String status){
        switch (status){
            case "delivering": return "status-shipping" ;
            case "delivered": return "status-success" ;
            case "returned": return "status-success" ;
            case "damage": return "status-failed" ;
            case "lost": return "status-failed" ;
            case "delivery_fail": return "status-failed" ;
            default:return "status-shipping";
        }
    }

    ShopModel getDataShop(String token) {
        List<ShopModel> shops = getShopByToken(token);
        GHNSetttingModel setttingModel = ghnSettingService.getData("KEYSETTING", GHNSetttingModel.class);
        for (ShopModel shopModel : shops) {
            for (GHNTokenModel tokenModel : setttingModel.getTokens()) {
                if (tokenModel.getShopId().equals(shopModel.get_id())) {
                    return shopModel;
                }
            }
        }
        return null;
    }
}
