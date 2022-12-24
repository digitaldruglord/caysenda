package com.nomi.caysenda.security;

import com.nomi.caysenda.exceptions.authentication.ForbiddenException;
import com.nomi.caysenda.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

@Service("detectDomainService")
public class DetectDomainService {
    @Autowired
    ProviderRepository providerRepository;
    public void detectDomain(String host) throws ForbiddenException {
//        if (!providerRepository.existsByHost(host)){
//            throw new ForbiddenException();
//        }

    }
}
