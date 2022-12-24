package com.nomi.caysenda.services;

import com.nomi.caysenda.redis.model.RedisProgress;

public interface ProgressService {
    void save(Integer current,Integer all,String code);
    void save(Integer current,Integer all,String code,Boolean running);
    void delete(String code);
    RedisProgress findByCode(String code);
}
