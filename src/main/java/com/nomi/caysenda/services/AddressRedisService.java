package com.nomi.caysenda.services;

import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.controller.responses.address.AddressResponse;
import com.nomi.caysenda.dto.AddressDTO;

import java.util.List;

public interface AddressRedisService {
    void save(AddressRequest addressRequest);
    void update(AddressRequest addressRequest);
    void delete(Integer id);
    AddressDTO findAddressDefault();
    AddressDTO findAddressById(Integer id);
    List<AddressDTO> findAllAddressDTOById();

}
