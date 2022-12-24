package com.nomi.caysenda.services;

import com.nomi.caysenda.api.admin.model.partner.PartnerRequest;
import com.nomi.caysenda.entity.ProviderEntity;

import java.util.List;

public interface PartnerService {
    List<ProviderEntity> findAll();
    ProviderEntity save(PartnerRequest partnerRequest);
    ProviderEntity update(PartnerRequest partnerRequest);
    void deleteById(Integer id);

}
