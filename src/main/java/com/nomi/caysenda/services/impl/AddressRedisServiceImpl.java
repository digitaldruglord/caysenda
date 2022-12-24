package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.controller.requests.address.AddressRequest;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.entity.AddressEntity;
import com.nomi.caysenda.redis.model.RedisAddress;
import com.nomi.caysenda.redis.repositories.RedisAddressRepository;
import com.nomi.caysenda.services.AddressRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressRedisServiceImpl implements AddressRedisService {
    @Autowired
    RedisAddressRepository addressRepository;

    @Override
    public void save(AddressRequest addressRequest) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        RedisAddress redisAddress = addressRepository.findBySessionIdAndActive(sessionId,true);
        if (redisAddress!=null) {
            redisAddress.setActive(false);
            addressRepository.save(redisAddress);
        }
        RedisAddress addressEntity = convertAdressRequestToEntity(addressRequest);
        addressEntity.setSessionId(sessionId);
        addressEntity.setActive(true);
        addressRepository.save(addressEntity);
    }
    private AddressDTO convertRedisAddressToAddressDTO(RedisAddress redisAddress){
        if (redisAddress==null) return null;
        AddressDTO addressDTO = new AddressDTO();
        if (redisAddress.getId()!=null) addressDTO.setId(redisAddress.getId());
        if (redisAddress.getPhoneNumber()!=null) addressDTO.setPhoneNumber(redisAddress.getPhoneNumber());
        if (redisAddress.getFullname()!=null) addressDTO.setFullname(redisAddress.getFullname());
        if (redisAddress.getEmail()!=null) addressDTO.setEmail(redisAddress.getEmail());
        if (redisAddress.getAddress()!=null) addressDTO.setAddress(redisAddress.getAddress());
        if (redisAddress.getProvince()!=null) addressDTO.setProvince(redisAddress.getProvince());
        if (redisAddress.getDictrict()!=null) addressDTO.setDictrict(redisAddress.getDictrict());
        if (redisAddress.getWard()!=null) addressDTO.setWard(redisAddress.getWard());
        if (redisAddress.getActive()!=null) addressDTO.setActive(redisAddress.getActive());
        return addressDTO;
    }
    private RedisAddress convertAdressRequestToEntity(AddressRequest request){
        RedisAddress redisAddress = new RedisAddress();
        if (request.getId()!=null) redisAddress.setId(request.getId());
        if (request.getPhoneNumber()!=null) redisAddress.setPhoneNumber(request.getPhoneNumber());
        if (request.getFullname()!=null) redisAddress.setFullname(request.getFullname());
        if (request.getEmail()!=null) redisAddress.setEmail(request.getEmail());
        if (request.getAddress()!=null) redisAddress.setAddress(request.getAddress());
        if (request.getProvince()!=null) redisAddress.setProvince(request.getProvince());
        if (request.getDictrict()!=null) redisAddress.setDictrict(request.getDictrict());
        if (request.getWard()!=null) redisAddress.setWard(request.getWard());
        return redisAddress;
    }
    @Override
    public void update(AddressRequest addressRequest) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public AddressDTO findAddressDefault() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        return convertRedisAddressToAddressDTO(addressRepository.findBySessionIdAndActive(sessionId,true));
    }

    @Override
    public AddressDTO findAddressById(Integer id) {
        if (id==null) return null;
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        RedisAddress redisAddress = addressRepository.findById(id).orElse(null);
        return convertRedisAddressToAddressDTO(redisAddress);
    }

    @Override
    public List<AddressDTO> findAllAddressDTOById() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        List<AddressDTO> list = new ArrayList<>();
        List<RedisAddress> redisAddresses = addressRepository.findAllBySessionId(sessionId);
        if (redisAddresses!=null){
            redisAddresses.forEach(redisAddress -> {
                list.add(convertRedisAddressToAddressDTO(redisAddress));
            });
        }
        return list;
    }
}
