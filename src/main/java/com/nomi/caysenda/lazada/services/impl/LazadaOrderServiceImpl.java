package com.nomi.caysenda.lazada.services.impl;

import com.google.gson.Gson;
import com.nomi.caysenda.api.admin.model.order.request.AdminOrderDetailt;
import com.nomi.caysenda.api.admin.model.order.request.AdminOrderRequest;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.lazada.api.LazopClient;
import com.nomi.caysenda.lazada.api.LazopRequest;
import com.nomi.caysenda.lazada.api.LazopResponse;
import com.nomi.caysenda.lazada.constants.LazadaContant;
import com.nomi.caysenda.lazada.entity.LazadaOrderEntity;
import com.nomi.caysenda.lazada.repositories.LazadaOrderRepository;
import com.nomi.caysenda.lazada.services.LazadaOrderService;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.lazada.services.model.LazadaOrderItem;
import com.nomi.caysenda.lazada.services.model.LazadaOrderItemResponse;
import com.nomi.caysenda.lazada.services.model.LazadaOrderResponse;
import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.repositories.VariantRepository;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.utils.ProductUtils;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LazadaOrderServiceImpl implements LazadaOrderService {
    @Autowired
    LazadaSystemService lazadaSystemService;
    @Autowired
    ProductService productService;
    @Autowired
    VariantRepository variantRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    LazadaOrderRepository lazadaOrderRepository;
    @Override
    public void getOrders() throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/orders/get");
        request.setHttpMethod("GET");
        request.addApiParameter("update_before", "2018-02-10T16:00:00+08:00");
        request.addApiParameter("sort_direction", "DESC");
        request.addApiParameter("offset", "0");
        request.addApiParameter("limit", "10");
        request.addApiParameter("update_after", "2017-02-10T09:00:00+08:00");
        request.addApiParameter("sort_by", "updated_at");
        request.addApiParameter("created_before", "2018-02-10T16:00:00+08:00");
        request.addApiParameter("created_after", "2017-02-10T09:00:00+08:00");
        request.addApiParameter("status", "shipped");
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());

    }

    @Override
    public LazadaOrderResponse getOrderById(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return null;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/get");
        request.setHttpMethod("GET");
        request.addApiParameter("order_id", id);
        LazopResponse response = client.execute(request, (String) token.get("token"));

        return new Gson().fromJson(response.getBody(),LazadaOrderResponse.class);
    }

    @Override
    public LazadaOrderItemResponse getOrderItems(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return null;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/items/get");
        request.setHttpMethod("GET");
        request.addApiParameter("order_id", id);
        LazopResponse response = client.execute(request, (String) token.get("token"));
        return new Gson().fromJson(response.getBody(), LazadaOrderItemResponse.class);
    }

    @Override
    public void setInvoiceNumber(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/invoice_number/set");
        request.addApiParameter("order_item_id",id );
        request.addApiParameter("invoice_number", "INV-20");
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setRepack(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/repack");
        request.addApiParameter("package_id", id);
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setStatusToCanceled(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/cancel");
        request.addApiParameter("reason_detail", "Out of stock");
        request.addApiParameter("reason_id", "15");
        request.addApiParameter("order_item_id", id);
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setStatusToPackedByMarketplace(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/pack");
        request.addApiParameter("shipping_provider", "Aramax");
        request.addApiParameter("delivery_type", "dropship");
        request.addApiParameter("order_item_ids", new Gson().toJson(List.of(id)));
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setStatusToReadyToShip(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/rts");
        request.addApiParameter("delivery_type", "dropship");
        request.addApiParameter("order_item_ids", new Gson().toJson(List.of(id)));
        request.addApiParameter("shipment_provider", "Aramax");
        request.addApiParameter("tracking_number", "12345678");
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setStatusToSOFDelivered(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/sof/delivered");
        request.addApiParameter("order_item_ids", new Gson().toJson(List.of(id)));
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void setStatusToSOFFailedDelivery(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/order/sof/failed_delivery");
        request.addApiParameter("order_item_ids", new Gson().toJson(List.of(id)));
        LazopResponse response = client.execute(request, (String) token.get("token"));
        System.out.println(response.getBody());
    }

    @Override
    public void create(String id) throws ApiException {
        getOrderById(id);
        if (!lazadaOrderRepository.existsByLazadaId(id)){
            LazadaOrderEntity lazadaOrderEntity = new LazadaOrderEntity();
            lazadaOrderEntity.setLazadaId(id);
            lazadaOrderEntity = lazadaOrderRepository.save(lazadaOrderEntity);

            LazadaOrderItemResponse lazadaOrderItemResponse = getOrderItems(id);
            if (lazadaOrderItemResponse.getCode()!=null && lazadaOrderItemResponse.getCode().equals("0")){
                List<LazadaOrderItem> orderItems =  lazadaOrderItemResponse.getData();
                /** order */
                AdminOrderRequest orderRequest = new AdminOrderRequest();
                orderRequest.setBillingFullName("Nguyen van hoang");
                orderRequest.setBillingPhoneNumber("0942492445");
                orderRequest.setBillingEmail("lakdak4@gmail.com");
                orderRequest.setOrderComment("Đơn hàng lazada");
                orderRequest.setShip(Long.valueOf(10000));
                orderRequest.setProductAmount(Long.valueOf(100000));
                orderRequest.setOrderAmount(Long.valueOf(100000));
                orderRequest.setCost(Long.valueOf(100000));
                orderRequest.setMethod("COD");
                orderRequest.setDiscountType("PERCENT");
                orderRequest.setDiscountValue(Double.valueOf(0));
                orderRequest.setStatus("processing");
                orderRequest.setNote("Đơn hàng lazada");

                /** detail*/
                List<AdminOrderDetailt> detailts = new ArrayList<>();
                for (LazadaOrderItem orderItem:orderItems){
                    VariantEntity variantEntity = variantRepository.findBySkuVi(orderItem.getSku());
                    ProductEntity productEntity = productService.findById(variantEntity.getProductEntity().getId());
                    if (!checkExistsAndUpdate(detailts,variantEntity)){
                        AdminOrderDetailt detailt = new AdminOrderDetailt();
                        /** category */
                        detailt.setCategoryId(productEntity.getCategoryDefault());
                        /** product */
                        detailt.setName(productEntity.getNameVi());
                        detailt.setProductId(productEntity.getId());
                        detailt.setProductThumbnail(productEntity.getThumbnail());
                        /** group */
                        GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                        /** variant */
                        detailt.setVariantId(variantEntity.getId());
                        detailt.setVariantName(variantEntity.getNameVi());
                        detailt.setVariantThumbnail(variantEntity.getThumbnail());
                        detailt.setQuantity(1);
                        detailt.setOwe(1);
                        detailt.setPrice(variantEntity.getPrice());
                        detailt.setPriceCN(variantEntity.getPriceZh());
                        detailt.setCost(variantEntity.getCost());
                        detailts.add(detailt);
                        System.out.println( variantEntity.getProductEntity().getId());
                    }

                }
                orderRequest.setDetailts(detailts);
                OrderEntity orderEntity = orderService.save(orderRequest);
                if (orderEntity!=null){

                    lazadaOrderEntity.setOrderId(orderEntity.getId());
                    lazadaOrderRepository.save(lazadaOrderEntity);

                }
             }
        }
    }
    boolean checkExistsAndUpdate(List<AdminOrderDetailt> detailts,VariantEntity variantEntity){
        for (AdminOrderDetailt detailt:detailts){
            if (detailt.getVariantId().equals(variantEntity.getId())){
                detailt.setQuantity(detailt.getQuantity()+1);
                detailt.setOwe(detailt.getQuantity());
                detailt.setCost(variantEntity.getCost());
                return true;
            }
        }
        return false;
    }
}
