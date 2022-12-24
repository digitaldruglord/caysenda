package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.api.admin.model.partner.PartnerRequest;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    ProviderRepository providerRepository;
    @Override
    public List<ProviderEntity> findAll() {
        return providerRepository.findAll();
    }

    @Override
    public ProviderEntity save(PartnerRequest partnerRequest) {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setProviderName(partnerRequest.getProviderName());
        providerEntity.setHost(partnerRequest.getHost());
        return providerRepository.save(providerEntity);
    }

    @Override
    public ProviderEntity update(PartnerRequest partnerRequest) {
        ProviderEntity providerEntity = providerRepository.findById(partnerRequest.getId()).orElse(null);
        if (providerEntity!=null){
            if (partnerRequest.getProviderName()!=null) providerEntity.setProviderName(partnerRequest.getProviderName());
            if (partnerRequest.getHost()!=null) providerEntity.setHost(partnerRequest.getHost());
            return providerRepository.save(providerEntity);
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        if (id!=null){
            providerRepository.deleteById(id);
        }
    }
}
