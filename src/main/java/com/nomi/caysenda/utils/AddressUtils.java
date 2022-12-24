package com.nomi.caysenda.utils;

import com.nomi.caysenda.controller.responses.address.AddressDataResponse;
import com.nomi.caysenda.entity.AddressDictrictEntity;
import com.nomi.caysenda.entity.AddressProviceEntity;
import com.nomi.caysenda.entity.AddressWardsEntity;

import java.util.ArrayList;
import java.util.List;

public class    AddressUtils {
    public static List<AddressDataResponse> convertEntityToDTO(List<AddressProviceEntity> provinces,
                                                               List<AddressDictrictEntity> dictricts,
                                                               List<AddressWardsEntity> wards){
        List<AddressDataResponse> list = new ArrayList<>();
        if (provinces!=null){
            provinces.forEach(addressProviceEntity -> list.add(new AddressDataResponse(addressProviceEntity.getId(),addressProviceEntity.getName())));
        }
        if (dictricts!=null){
            dictricts.forEach(addressDictrictEntity -> list.add(new AddressDataResponse(addressDictrictEntity.getId(), addressDictrictEntity.getName())));
        }
        if (wards!=null){
            wards.forEach(addressWardsEntity -> list.add(new AddressDataResponse(addressWardsEntity.getId(), addressWardsEntity.getName())));
        }
        return list;
    }
}
