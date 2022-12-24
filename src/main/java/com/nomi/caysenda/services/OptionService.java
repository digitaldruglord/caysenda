package com.nomi.caysenda.services;

import com.nomi.caysenda.entity.OptionEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionService {
    @Cacheable(value = "options")
    OptionEntity findByCode(String optionKey,String host);
    @CachePut(value = "options")
    OptionEntity save(OptionEntity optionEntity);
    void delete(OptionEntity optionEntity);
    <T> T getData(String optionKey,Class<T> tClass);
    <T> void update(T t,String optionKey);
    void delete(String optionKey);

}
