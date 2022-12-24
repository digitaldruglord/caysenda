package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.controller.responses.address.AddressResponse;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.AddressRedisService;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.utils.AddressUtils;
import org.apache.commons.math3.analysis.function.Add;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    AddressProvinceRepository provinceRepository;
    @Autowired
    AddressDictrictRepository dictrictRepository;
    @Autowired
    AddressWardRepository wardRepository;
    @Autowired
    AddressRedisService addressRedisService;
    @Override
    public void save(AddressRequest addressRequest) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null){
            UserEntity userEntity = userRepository.findById(userDetail.getUserId()).get();
            AddressEntity addressDefault = addressRepository.findByDefaultAddressEntity(userDetail.getUserId());
            if (addressDefault!=null) {
                addressDefault.setActive(false);
                addressRepository.save(addressDefault);
            }
            AddressEntity addressEntity = convertAdressRequestToEntity(addressRequest);
            addressEntity.setUserAddress(userEntity);
            addressEntity.setActive(true);

            addressRepository.save(addressEntity);
        }else {
            addressRedisService.save(addressRequest);
        }

    }

    @Override
    public void update(AddressRequest addressRequest) {

    }

    @Override
    public void delete(Integer id) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        AddressEntity addressEntity = addressRepository.findByIdAndUserAddress_Id(id,userDetail.getUserId());
        if (addressEntity!=null){
            addressRepository.delete(addressEntity);
        }

    }

    @Override
    public AddressDTO findAddressDefault(Integer userId) {
        return addressRepository.findByDefaultAddress(userId);
    }

    @Override
    public AddressResponse getModal(Integer addressId) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        Context context = new Context();
        if (userDetail!=null){
            if (addressId!=null){
                AddressEntity addressEntity = addressRepository.findByIdAndUserAddress_Id(addressId,userDetail.getUserId());
                context.setVariable("address",addressEntity);
                context.setVariable("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
                context.setVariable("dictricts",AddressUtils.convertEntityToDTO(null,dictrictRepository.findAllByProviceEntity_Id(addressEntity.getProvince()),null));
                context.setVariable("wards",AddressUtils.convertEntityToDTO(null,null,wardRepository.findAllByDictrictEntity_Id(addressEntity.getDictrict())));
            }else {
                context.setVariable("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
                context.setVariable("address",new AddressEntity());
            }
        }else {
            AddressDTO addressDTO = addressRedisService.findAddressById(addressId);
            if (addressDTO!=null){
                context.setVariable("address",addressDTO);
                context.setVariable("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
                context.setVariable("dictricts",AddressUtils.convertEntityToDTO(null,dictrictRepository.findAllByProviceEntity_Id(addressDTO.getProvince()),null));
                context.setVariable("wards",AddressUtils.convertEntityToDTO(null,null,wardRepository.findAllByDictrictEntity_Id(addressDTO.getDictrict())));

            }else {
                context.setVariable("provinces", AddressUtils.convertEntityToDTO(provinceRepository.findAll(),null,null));
                context.setVariable("address",new AddressEntity());

            }
        }

        context.setVariable("ref","address");

        return new AddressResponse(true,templateEngine.process("fragment/address/modal-create",context));
    }

    @Override
    public List<AddressDTO> findAllAddressDTOById() {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        List<AddressDTO> list = new ArrayList<>();
        if (userDetail!=null){
            list = addressRepository.findAllAddressDTOByUserAddress_Id(userDetail.getUserId());
            list.forEach(addressDTO -> {
                getFullDataAddress(addressDTO);
            });
        }else {
            list = addressRedisService.findAllAddressDTOById();
            list.forEach(addressDTO -> {
                getFullDataAddress(addressDTO);
            });
        }
        return list;
    }

    @Override
    public List<AddressProviceEntity> provinces() {
        return provinceRepository.findAll();
    }

    @Override
    public List<AddressDictrictEntity> dictrcits(String provinceId) {
        return dictrictRepository.findAllByProviceEntity_Id(provinceId);
    }

    @Override
    public List<AddressWardsEntity> wards(String dictrictId) {
        return wardRepository.findAllByDictrictEntity_Id(dictrictId);
    }

    @Override
    public void setDefaultAddress(Integer id) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null){
            List<AddressEntity> addressEntityList = addressRepository.findAllByUserAddress_Id(userDetail.getUserId());
            addressEntityList.forEach(addressEntity -> {
                if (addressEntity.getId().equals(id)){
                    addressEntity.setActive(true);
                }else {
                    addressEntity.setActive(false);
                }
                addressRepository.save(addressEntity);
            });
        }

    }

    @Override
    public String getFullAddress(String address, String province, String dictrict, String ward) {
        StringBuilder builder = new StringBuilder(address!=null?address:"");
        AddressProviceEntity proviceEntity = provinceRepository.findById(province).orElse(null);
        AddressDictrictEntity dictrictEntity = dictrictRepository.findById(dictrict).orElse(null);
        AddressWardsEntity wardsEntity = wardRepository.findById(ward).orElse(null);
        if (wardsEntity!=null) builder.append(",").append(wardsEntity.getName());
        if (dictrictEntity!=null)builder.append(",").append(dictrictEntity.getName());
        if (proviceEntity!=null)builder.append(",").append(proviceEntity.getName());
        return builder.toString();
    }


    private void getFullDataAddress(AddressDTO addressDTO){
        AddressProviceEntity proviceEntity = provinceRepository.findById(addressDTO.getProvince()).orElse(null);
        AddressDictrictEntity dictrictEntity = dictrictRepository.findById(addressDTO.getDictrict()).orElse(null);
        AddressWardsEntity addressWardsEntity = wardRepository.findById(addressDTO.getWard()).orElse(null);
        addressDTO.setProvince(proviceEntity!=null?proviceEntity.getName():"");
        addressDTO.setDictrict(dictrictEntity!=null?dictrictEntity.getName():"");
        addressDTO.setWard(addressWardsEntity!=null?addressWardsEntity.getName():"");
        addressDTO.setFullAddress(addressDTO.getAddress()+"-"+addressDTO.getWard()+"-"+addressDTO.getDictrict()+"-"+addressDTO.getProvince());
    }


    private AddressEntity convertAdressRequestToEntity(AddressRequest request){
        AddressEntity addressEntity = new AddressEntity();
        if (request.getId()!=null) addressEntity.setId(request.getId());
        if (request.getPhoneNumber()!=null) addressEntity.setPhoneNumber(request.getPhoneNumber());
        if (request.getFullname()!=null) addressEntity.setFullname(request.getFullname());
        if (request.getEmail()!=null) addressEntity.setEmail(request.getEmail());
        if (request.getAddress()!=null) addressEntity.setAddress(request.getAddress());
        if (request.getProvince()!=null) addressEntity.setProvince(request.getProvince());
        if (request.getDictrict()!=null) addressEntity.setDictrict(request.getDictrict());
        if (request.getWard()!=null) addressEntity.setWard(request.getWard());
        return addressEntity;
    }
}
