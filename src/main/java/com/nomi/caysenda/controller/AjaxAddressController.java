package com.nomi.caysenda.controller;

import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.controller.responses.address.AddressResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface AjaxAddressController {
    @GetMapping()
    ResponseEntity<List> getData(@RequestParam(value = "province",required = false) String provinceId,
                                 @RequestParam(value = "dictrict",required = false) String dictrict);
    @PostMapping()
    ResponseEntity<AddressResponse> create(@RequestBody AddressRequest addressRequest);
    @PutMapping()
    ResponseEntity<AddressResponse> update(@RequestBody AddressRequest addressRequest);
    @DeleteMapping()
    ResponseEntity<AddressResponse> delete(@RequestParam("addressId") Integer addressId);
    @GetMapping("/load-modal")
    ResponseEntity<AddressResponse> modal(@RequestParam(value = "id",required = false) Integer id,
                                          @RequestParam("ref") String ref);
    @GetMapping("/default-address")
    ResponseEntity<Map> defaultAddress(@RequestParam(value = "id",required = false) Integer id);
}
