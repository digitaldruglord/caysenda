package com.nomi.caysenda.services;


import com.nomi.caysenda.ghtk.models.PickModelGHTK;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public interface GiaoHangTietKiemService {
    Map<String,Object> getFeeShip(String province, String district, String address, String weight);
    Map<String,Object> getFeeShip(String weight);
    List<PickModelGHTK> getListPick();
    PickModelGHTK getPickAddressById(String id);
    ResponseEntity<Map> queryTransactionStatus(String id);
    Map<String,Object> cancelOrder(String id);
    Map<String,Object> cancelOrderByPartnerID(String id);
    ResponseEntity<byte[]> getLabel(String id);
}
