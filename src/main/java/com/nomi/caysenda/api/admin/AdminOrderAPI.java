package com.nomi.caysenda.api.admin;

import com.nomi.caysenda.api.admin.model.order.request.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AdminOrderAPI {
    @GetMapping()
    ResponseEntity<Map> orders(@RequestParam(value = "page",required = false) Integer page,
                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
                               @RequestParam(value = "status",required = false) String status,
                               @RequestParam(value = "trash",required = false) Boolean trash,
                               @RequestParam(value = "host",required = false) Integer host,
                               @RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "from",required = false) String from,
                               @RequestParam(value = "to",required = false) String to);

    @GetMapping("/findbyid")
    ResponseEntity<Map> findById(@RequestParam("id") Integer id);
    @PostMapping()
    ResponseEntity<Map> create(@RequestBody AdminOrderRequest orderRequest);
    @PostMapping("/statictis-product")
    ResponseEntity<Map> statictisProduct(@RequestBody AdminOrderRequest orderRequest);
    @PutMapping()
    ResponseEntity<Map> update();
    @DeleteMapping()
    ResponseEntity<Map> delete();
    @GetMapping("/change-status")
    ResponseEntity<Map> changeStatus();
    @GetMapping("/print")
    ResponseEntity<Map> printOrder();
    @GetMapping("/get-status")
    ResponseEntity<Map> getStatus();
    @GetMapping("/get-method")
    ResponseEntity<Map> getMethods();
    @GetMapping("/get-method-status")
    ResponseEntity<Map> getMethodsAndStatus();
    @PostMapping("/actions")
    ResponseEntity<Map> actions(@RequestBody OrderActionRequest actionRequest);
    @RequestMapping("/quickview-update")
    ResponseEntity<Map> quickViewUpdate(@RequestBody AdminOrderQuickviewRequest quickviewRequest);
    @RequestMapping("/countbystatus")
    ResponseEntity<Map> countByStatus(@RequestParam("status") String status);
    @RequestMapping("/cart-statictis")
    ResponseEntity<Map> statictisCart(@RequestParam(value = "page",required = false) Integer page,
                                      @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                      @RequestParam(value = "keyword",required = false) String keyword,
                                      @RequestParam(value = "sort",required = false) String sort);
    @GetMapping("/cart-detailt")
    ResponseEntity<Map> statictisCart(@RequestParam("id") Integer id);
    @PostMapping("/cart-detailt")
    ResponseEntity<Map> updateCartNote(@RequestParam("id") Integer id,@RequestParam("note") String note);
    @GetMapping("/tracking")
    ResponseEntity<Map> tracking(@RequestParam("orderId") Integer orderId);
    @PostMapping("/tracking")
    ResponseEntity<Map> updateTracking(@RequestBody List<TrackingOrderRequest> requests);
    @GetMapping("/track-package")
    ResponseEntity<Map> updateTrackPackage(@RequestParam("trackId") Integer trackId,
                                           @RequestParam("trackPackage") Integer trackPackage);
    @DeleteMapping("/tracking")
    ResponseEntity<Map> deleteTracking(@RequestParam("trackId") Integer id);
    @GetMapping("/exchange-rate-tracking")
    ResponseEntity<Map> getExchangeRateTracking();
    @PostMapping("/exchange-rate-tracking")
    ResponseEntity<Map> updateExchangeRateTracking(@RequestParam("value") Double value);
    @GetMapping("/findallbyladingcode")
    ResponseEntity<Map> findAllByLaddingCode(@RequestParam("laddingcode")String laddingCode);
    @GetMapping("/tracking-getproduct")
    ResponseEntity<Map> trackingGetProduct(@RequestParam("id")Integer id,
                                           @RequestParam("packagereceived") Integer quantity);
    @GetMapping("/export-track")
    ResponseEntity<InputStreamResource> exportTrack(@RequestParam("orderId") List<Integer> ids,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) throws IOException;
    @PostMapping("/split")
    ResponseEntity<Map> split(@RequestBody AdminOrderSplitRequest iSplit);
}
