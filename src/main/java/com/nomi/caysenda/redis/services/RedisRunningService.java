package com.nomi.caysenda.redis.services;

import com.nomi.caysenda.redis.model.RedisRunning;

public interface RedisRunningService {
    void save(String taskcode);
    void delete(String taskCode);
    RedisRunning findByTaskCode(String taskCode);
}
