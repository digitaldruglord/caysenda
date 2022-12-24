package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisProgress;
import org.springframework.data.repository.CrudRepository;

public interface RedisProgressRepository extends CrudRepository<RedisProgress,String> {
    RedisProgress findByTaskCodeAndHost(String taskCode,String host);
}
