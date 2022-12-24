package com.nomi.caysenda.api.admin.impl;


import com.nomi.caysenda.api.admin.AdminOrderAPI;
import com.nomi.caysenda.api.admin.model.order.request.*;
import com.nomi.caysenda.dto.OrderAdminDTO;
import com.nomi.caysenda.dto.TrackingOrderDTO;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.repositories.TrackingOrderRepository;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.services.CartService;
import com.nomi.caysenda.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderAPIImpl implements AdminOrderAPI {
    @Autowired
    OrderService orderService;
    @Autowired
    AddressService addressService;
    @Autowired
    CartService cartService;
    @Autowired
    TrackingOrderRepository trackingOrderRepository;
    @Autowired GHNService ghnService;
    @Override
    public ResponseEntity<Map> orders(Integer page, Integer pageSize, String status, Boolean trash, Integer host, String keyword, String from, String to) {
        Map map = new HashMap();
        map.put("success", true);
        Pageable pageable = PageRequest.of(page != null ? page : 0, pageSize != null ? pageSize : 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<OrderAdminDTO> data = orderService.findAllForOrderAdminDTO(status, trash, host, keyword, from,to, pageable);
        List<Integer> ids = data.getContent().stream().map(orderAdminDTO -> orderAdminDTO.getId()).collect(Collectors.toList());
        List<TrackingOrderDTO> tracking = trackingOrderRepository.findAllByIds(ids);
        List<GHNOrderEntity> ghn = ghnService.findByIds(ids);
        map.put("data", data);
        map.put("tracking", tracking);
        map.put("ghn", ghn);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> findById(Integer id) {
        Map map = new HashMap();
        OrderEntity orderEntity = orderService.findById(id);
        map.put("order", orderEntity);
        map.put("fulladdress", orderService.getFullAddress(orderEntity.getBillingAddress(),orderEntity.getBillingCity(),orderEntity.getBillingDistrict(),orderEntity.getBillingWards()));
        map.put("detailts", orderService.findDetailForAdmin(id));
        map.put("dictricts", addressService.dictrcits(orderEntity.getBillingCity()));
        map.put("wards", addressService.wards(orderEntity.getBillingDistrict()));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> create(AdminOrderRequest orderRequest) {
        Map map = new HashMap();
        OrderEntity orderEntity = orderService.save(orderRequest);
        map.put("order", orderEntity);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> statictisProduct(AdminOrderRequest orderRequest) {
        List<Map> list = orderService.statictisProduct(orderRequest);
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    @Override
    public ResponseEntity<Map> update() {
        return null;
    }

    @Override
    public ResponseEntity<Map> delete() {
        return null;
    }

    @Override
    public ResponseEntity<Map> changeStatus() {
        return null;
    }

    @Override
    public ResponseEntity<Map> printOrder() {
        return null;
    }

    @Override
    public ResponseEntity<Map> getStatus() {
        return ResponseEntity.ok(orderService.getStatus());
    }

    @Override
    public ResponseEntity<Map> getMethods() {
        return ResponseEntity.ok(orderService.getMethod());
    }

    @Override
    public ResponseEntity<Map> getMethodsAndStatus() {
        return ResponseEntity.ok(Map.of("methods", orderService.getMethod(), "status", orderService.getStatus(), "provinces", addressService.provinces()));
    }

    @Override
    public ResponseEntity<Map> actions(OrderActionRequest actionRequest) {
       Map map = new HashMap();
        map.put("success",true);
        switch (actionRequest.getAction()) {
            case "remove": orderService.deleteByIds(actionRequest.getIds());break;
            case "trash": orderService.addToTrashByIds(actionRequest.getIds());break;
            case "print": break;
            case "pendding":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "pendding")) ;break;
            case "cancel":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "cancel")) ;break;
            case "success":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "success")) ;break;
            case "processing":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "processing")) ;break;
            case "shipping":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "shipping")) ;break;
            case "awaitingadditionaldelivery":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "awaitingadditionaldelivery")) ;break;
            case "partiallypaid":map.put("data", orderService.changeStatusByIds(actionRequest.getIds(), "partiallypaid"));break;
            case "paid":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(), "paid")) ;break;
            case "restore": orderService.restoreByIds(actionRequest.getIds());break;
            case "purchased":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(),"purchased")) ;break;
            case "waitingreceived":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(),"waitingreceived")) ;break;
            case "deliveredall":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(),"deliveredall")) ;break;
            case "customerreceived":map.put("data",orderService.changeStatusByIds(actionRequest.getIds(),"customerreceived")) ;break;
            case "merge":map.put("data",orderService.merge(actionRequest.getIds().get(0), actionRequest.getIds().get(1))) ;break;
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> quickViewUpdate(AdminOrderQuickviewRequest quickviewRequest) {
        orderService.update(quickviewRequest);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @Override
    public ResponseEntity<Map> countByStatus(String status) {
        return ResponseEntity.ok(Map.of("success", true, "data", orderService.countByStatus(status)));
    }

    @Override
    public ResponseEntity<Map> statictisCart(Integer page, Integer pageSize, String keyword, String sort) {

        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20);
        return ResponseEntity.ok(Map.of("success", true, "data", cartService.statictisCart(sort,keyword , pageable)));
    }

    @Override
    public ResponseEntity<Map> statictisCart(Integer id) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", cartService.getCart(id),
                "summary", cartService.statictisCart(id)
                )

        );
    }

    @Override
    public ResponseEntity<Map> updateCartNote(Integer id, String note) {
        cartService.updateCartNote(id,note);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @Override
    public ResponseEntity<Map> tracking(Integer orderId) {
        return ResponseEntity.ok(Map.of("success",true,"data",orderService.getTracking(orderId)));
    }

    @Override
    public ResponseEntity<Map> updateTracking(List<TrackingOrderRequest> requests) {
        orderService.updateTrackingOrder(requests);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateTrackPackage(Integer trackId, Integer trackPackage) {
        orderService.updateTrackPackage(trackId,trackPackage);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteTracking(Integer id) {
        orderService.deleteTrackingOrder(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getExchangeRateTracking() {
        return ResponseEntity.ok(Map.of("success",true,"data",orderService.exchangeRateTracking()));
    }

    @Override
    public ResponseEntity<Map> updateExchangeRateTracking(Double value) {
        orderService.updateExchangeRateTracking(value);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> findAllByLaddingCode(String laddingCode) {
        return ResponseEntity.ok(Map.of("success",true,"data",orderService.findAllByLaddingCode(laddingCode)));
    }

    @Override
    public ResponseEntity<Map> trackingGetProduct(Integer id, Integer quantity) {
        return ResponseEntity.ok(Map.of("success",orderService.trackReceiveProduct(id,quantity )));
    }

    @Override
    public ResponseEntity<InputStreamResource> exportTrack(List<Integer> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=xuat-file-van-chuyen.xlsx");
        byte[] bytes = orderService.generateExcelTrack(ids);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Map> split(AdminOrderSplitRequest iSplit) {
		return ResponseEntity.ok(Map.of("success",true, "data", orderService.split(iSplit)));
	}

}
