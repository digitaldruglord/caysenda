package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.entity.OptionEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.repositories.OptionRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.OptionService;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Service("optionService")
public class OptionServiceImpl implements OptionService {
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    Environment env;
    @Autowired
    ProviderRepository providerRepository;

    @Override
    public OptionEntity findByCode(String optionKey, String host) {
        return optionRepository.findByCode(optionKey,host);
    }

    @Override
    public OptionEntity save(OptionEntity optionEntity) {
        return  optionRepository.save(optionEntity);
    }

    @Override
    public void delete(OptionEntity optionEntity) {

    }

    @Override
    public <T> T getData(String optionKey, Class<T> tClass) {
        OptionEntity entity = findByCode(optionKey,env.getProperty("spring.domain"));
        if (entity == null) return null;
        return SpringUtils.convertJsonToObject(entity.getData(), tClass);
    }

    @Override
    public <T> void update(T t, String optionKey) {
        OptionEntity entity = findByCode(optionKey, env.getProperty("spring.domain"));
        ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
        if (entity == null) {

            entity = new OptionEntity();
            entity.setCode(optionKey);
            entity.setData(SpringUtils.convertObjectToJson(t));
            entity.setProviderOption(List.of(providerEntity));

        } else {
            List<ProviderEntity> providerEntities = providerRepository.findAllByOptionId(entity.getId());
            if (providerEntities.stream().filter(providerEntityStream -> providerEntityStream.getHost().equals(env.getProperty("spring.domain"))).collect(Collectors.toList()).size()<=0){
                providerEntities.add(providerEntity);
            }
            entity.setProviderOption(providerEntities);
            entity.setData(SpringUtils.convertObjectToJson(t));

        }
       save(entity);
    }

    @Override
    public void delete(String optionKey) {
        OptionEntity optionEntity = findByCode(optionKey,env.getProperty("spring.domain"));
        optionRepository.delete(optionEntity);
    }
}
