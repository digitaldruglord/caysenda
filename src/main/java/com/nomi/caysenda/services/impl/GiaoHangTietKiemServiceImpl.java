package com.nomi.caysenda.services.impl;


import com.nomi.caysenda.ghtk.GHTK;
import com.nomi.caysenda.ghtk.models.PickModelGHTK;
import com.nomi.caysenda.services.GiaoHangTietKiemService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.utils.GHTKUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GiaoHangTietKiemServiceImpl implements GiaoHangTietKiemService {
    @Autowired
    OrderService orderService;


    private String URL_INVOICE = "/ghtk/invoice?id=";
    private String Prefix="GHTKNGOCMINH_";
    HttpHeaders headers = new HttpHeaders();
    public GiaoHangTietKiemServiceImpl() {
        headers.add("Token", "6df4892cF954b8A37EbaA83Db49A80459c89647a");
        headers.add("Content-Type", "application/json");
    }


    @Override
    public Map<String,Object> getFeeShip(String province,String district,String address,String weight) {
        Map<String,Object> map = new HashMap<>();
        MultiValueMap<String,String> request = new HttpHeaders();
        PickModelGHTK pickModelGHTK = getPickAddressById(null);
        request.add("pick_province",pickModelGHTK.getPick_province());
        request.add("pick_district",pickModelGHTK.getPick_district());
        request.add("pick_address",pickModelGHTK.getPick_address());
        request.add("province",province);
        request.add("district",district);
        request.add("address",address);
        request.add("weight",weight);
        ResponseEntity<Map> responseEntity = GHTK.getFeeShip(request,headers);
        Map<String,Object> response = responseEntity.getBody();
        Map<String,Object> objectResponse = getFeeShipFromResponse(response);
        boolean status = (boolean) objectResponse.get("status");
        if (status){
            map.putAll(getFeeShipFromResponse(response));
        }else {
            map.putAll(getFeeShip(weight));
        }
        return map;
    }

    @Override
    public Map<String,Object> getFeeShip(String weight) {
        Map<String,Object> map = new HashMap<>();
        MultiValueMap<String,String> request = new HttpHeaders();
        Map<String,String> pickInfo = GHTK.getPickInfoDefault();
        PickModelGHTK pickModelGHTK = getPickAddressById(null);
        request.add("pick_province",pickModelGHTK.getPick_province());
        request.add("pick_district",pickModelGHTK.getPick_district());
        request.add("pick_address",pickModelGHTK.getPick_address());
        request.add("province",pickInfo.get("province"));
        request.add("district",pickInfo.get("district"));
        request.add("address",pickInfo.get("address"));
        request.add("weight",weight);
        ResponseEntity<Map> responseEntity = GHTK.getFeeShip(request,headers);
        Map<String,Object> response = responseEntity.getBody();
        map.putAll(getFeeShipFromResponse(response));
        return map;
    }

    @Override
    public List<PickModelGHTK> getListPick() {
        ResponseEntity<Map> responseEntity = GHTK.list_pick(headers);
        boolean success = (boolean) responseEntity.getBody().get("success");
        List<Map<String, Object>> data = (List<Map<String, Object>>) responseEntity.getBody().get("data");
        if (success){
            return  GHTKUtils.convertResponsePickToModelGHTK(data);
        }
        return null;
    }

    @Override
    public PickModelGHTK getPickAddressById(String id) {
        List<PickModelGHTK> listPick = getListPick();
        if (listPick!=null && listPick.size()>0){
            if (id==null){
                return listPick.get(0);
            }
            for (PickModelGHTK pickModelGHTK:listPick){
                if (pickModelGHTK.getPick_address_id().equals(id)){
                    return pickModelGHTK;
                }
            }
        }
        return null;
    }


    private Map<String,Object> getFeeShipFromResponse(Map<String,Object> response){
        Map<String,Object> map = new HashMap<>();
        boolean status = (boolean) response.get("success");
        if (status){
            LinkedHashMap<String,Object> fee = (LinkedHashMap<String, Object>) response.get("fee");
            boolean delivery = (boolean) fee.get("delivery");
            if (delivery){
                Integer feeInteger = (Integer) fee.get("fee");
                Long feeShip = Long.valueOf(feeInteger);
                map.put("status",true);
                map.put("fee",feeShip);
            }else {
                map.put("status",false);
            }
        }else {
            map.put("status",false);
        }
        return map;
    }
    @Override
    public ResponseEntity<Map> queryTransactionStatus(String id) {
        return GHTK.getStatusOrder(id,headers);

    }

    @Override
    public Map<String, Object> cancelOrder(String id) {
        return null;
    }

    @Override
    public Map<String, Object> cancelOrderByPartnerID(String id) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getLabel(String id) {
        ResponseEntity<byte[]> responseEntity = GHTK.printOrderLabel(id,headers);
        return responseEntity;
    }


}
