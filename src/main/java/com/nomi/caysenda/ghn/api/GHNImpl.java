package com.nomi.caysenda.ghn.api;

import com.nomi.caysenda.ghn.model.request.CreateOrderModelRequest;
import com.nomi.caysenda.ghn.model.request.FeeModelRequest;
import com.nomi.caysenda.ghn.model.request.ServiceModelRequset;
import com.nomi.caysenda.ghn.model.request.StoreModelRequest;
import com.nomi.caysenda.ghn.model.responses.*;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class GHNImpl implements GHN{
    @Autowired
    RestTemplate restTemplate;
    @Override
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token","939197f9-c03c-11ea-bbfa-9228745a132b");
        return headers;
    }

    @Override
    public ServiceModelResponse getService(String token, ServiceModelRequset requset) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity<ServiceModelRequset> entity = new HttpEntity<ServiceModelRequset>(requset,headers);
        ResponseEntity<ServiceModelResponse> responseEntity = restTemplate.exchange(GHNURL.GET_SERVICE, HttpMethod.POST,entity,ServiceModelResponse.class);
        return responseEntity.getBody();
    }

    @Override
    public StoreModelResponse getStore(StoreModelRequest request,String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<StoreModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.GET_STORE, HttpMethod.GET,entity,StoreModelResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){

        }
        if (responseEntity==null) return null;
        return responseEntity.getBody();
    }

    @Override
    public PickShiftModelResponse getPickShift(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<PickShiftModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.PICK_SHIFT, HttpMethod.GET,entity,PickShiftModelResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){

        }
        if (responseEntity==null) return null;
        return responseEntity.getBody();
    }

    @Override
    public FeeModelResponse getFee(String token, FeeModelRequest modelRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity<FeeModelRequest> entity = new HttpEntity<>(modelRequest,headers);
        ResponseEntity<FeeModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.CANCULATE_FEE, HttpMethod.POST,entity,FeeModelResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){
            return SpringUtils.convertJsonToObject(httpClientErrorException.getResponseBodyAsString(),FeeModelResponse.class);
        }

        if (responseEntity==null) return null;
        return responseEntity.getBody();

    }

    @Override
    public CreateOrderModelResponse createOrder(String token, CreateOrderModelRequest modelRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity<CreateOrderModelRequest> entity = new HttpEntity<CreateOrderModelRequest>(modelRequest,headers);
        ResponseEntity<CreateOrderModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.CREATE_ORDER, HttpMethod.POST,entity,CreateOrderModelResponse.class);

        }catch (HttpClientErrorException httpClientErrorException){
           return SpringUtils.convertJsonToObject(httpClientErrorException.getResponseBodyAsString(),CreateOrderModelResponse.class);
        }
        return responseEntity.getBody();
    }

    @Override
    public UpdateModelResponse updateOrder(String token, CreateOrderModelRequest modelRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity<CreateOrderModelRequest> entity = new HttpEntity<CreateOrderModelRequest>(modelRequest,headers);
        ResponseEntity<UpdateModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.UPDATE_ORDER, HttpMethod.POST,entity,UpdateModelResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){
            return SpringUtils.convertJsonToObject(httpClientErrorException.getResponseBodyAsString(),UpdateModelResponse.class);
        }
        return responseEntity.getBody();
    }

    @Override
    public OrderInfoModelResponse getOrder(String token, String order_code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<OrderInfoModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.INFO_ORDER+"?order_code="+order_code, HttpMethod.GET,entity,OrderInfoModelResponse.class);

        }catch (HttpClientErrorException httpClientErrorException){
            return SpringUtils.convertJsonToObject(httpClientErrorException.getResponseBodyAsString(),OrderInfoModelResponse.class);
        }
        return responseEntity.getBody();
    }

    @Override
    public String print(String token, String order_code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<PrintModelResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.PRINT_ORDER+"?order_codes="+order_code, HttpMethod.GET,entity,PrintModelResponse.class);
            ResponseEntity<String> template = restTemplate.exchange("https://online-gateway.ghn.vn/a5/public-api/printA5?token="+responseEntity.getBody().getData().getToken(), HttpMethod.GET,entity,String.class);
            return template.getBody();
        }catch (HttpClientErrorException httpClientErrorException){

        }
        return null;

    }

    @Override
    public GHNBaseResponse cancel(String token, String order_code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Token",token);
        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<GHNBaseResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(GHNURL.CANCEL_ORDER+"?order_codes="+order_code, HttpMethod.GET,entity,GHNBaseResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){
            return SpringUtils.convertJsonToObject(httpClientErrorException.getResponseBodyAsString(),GHNBaseResponse.class);
        }

        return responseEntity.getBody();
    }

}
