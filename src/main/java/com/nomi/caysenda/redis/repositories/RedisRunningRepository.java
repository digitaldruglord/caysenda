package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisRunning;
import org.springframework.data.repository.CrudRepository;

public interface RedisRunningRepository extends CrudRepository<RedisRunning,String> {
    RedisRunning findByTaskCodeAndHost(String taskCode,String host);
}
