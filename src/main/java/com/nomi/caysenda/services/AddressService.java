package com.nomi.caysenda.services;

import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.controller.responses.address.AddressResponse;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.entity.AddressDictrictEntity;
import com.nomi.caysenda.entity.AddressProviceEntity;
import com.nomi.caysenda.entity.AddressWardsEntity;

import java.util.List;

public interface AddressService {
    void save(AddressRequest addressRequest);
    void update(AddressRequest addressRequest);
    void delete(Integer id);
    AddressDTO findAddressDefault(Integer userId);
    AddressResponse getModal(Integer addressId);
    List<AddressDTO> findAllAddressDTOById();
    List<AddressProviceEntity> provinces();
    List<AddressDictrictEntity> dictrcits(String provinceId);
    List<AddressWardsEntity> wards(String dictrictId);
    void setDefaultAddress(Integer id);
    String getFullAddress(String address,String province,String dictrict,String ward);
}