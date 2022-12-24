package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.AjaxAddressController;
import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.controller.responses.address.AddressResponse;
import com.nomi.caysenda.controller.responses.payment.PaymentResponse;
import com.nomi.caysenda.repositories.AddressDictrictRepository;
import com.nomi.caysenda.repositories.AddressProvinceRepository;
import com.nomi.caysenda.repositories.AddressWardRepository;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.utils.AddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ajax/address")
public class AjaxAddressControllerImpl implements AjaxAddressController {
    @Autowired AddressProvinceRepository provinceRepository;
    @Autowired AddressDictrictRepository dictrictRepository;
    @Autowired AddressWardRepository wardRepository;
    @Autowired AddressService addressService;
    @Autowired OrderService orderService;
    @Override
    public ResponseEntity<List> getData(String provinceId, String dictrict) {
        if (provinceId!=null){
            return ResponseEntity.ok(AddressUtils.convertEntityToDTO(null,dictrictRepository.findAllByProviceEntity_Id(provinceId),null));
        }else if (dictrict!=null){
            return ResponseEntity.ok(AddressUtils.convertEntityToDTO(null,null,wardRepository.findAllByDictrictEntity_Id(dictrict)));
        }else {
            return ResponseEntity.ok(AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
        }
    }

    @Override
    public ResponseEntity<AddressResponse> create(AddressRequest addressRequest) {
        addressService.save(addressRequest);
        PaymentResponse paymentResponse = new PaymentResponse();
        if (addressRequest.getRef().equals("payment")){
            paymentResponse = orderService.confirm("COD","");

        }
        return ResponseEntity.ok(new AddressResponse(true,"asdasd",paymentResponse.getMessage()));
    }

    @Override
    public ResponseEntity<AddressResponse> update(AddressRequest addressRequest) {
        addressService.save(addressRequest);
        return ResponseEntity.ok(new AddressResponse(true,"asdasd"));
    }

    @Override
    public ResponseEntity<AddressResponse> delete(Integer addressId) {
        addressService.delete(addressId);
        return ResponseEntity.ok(new AddressResponse(true,"asdasd"));
    }

    @Override
    public ResponseEntity<AddressResponse> modal(Integer id, String ref) {
        return ResponseEntity.ok(addressService.getModal(id));
    }

    @Override
    public ResponseEntity<Map> defaultAddress(Integer id) {
        addressService.setDefaultAddress(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

}
