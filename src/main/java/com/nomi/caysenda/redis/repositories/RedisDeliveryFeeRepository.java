package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisDeliveryFee;
import org.springframework.data.repository.CrudRepository;

public interface RedisDeliveryFeeRepository extends CrudRepository<RedisDeliveryFee,String> {
    RedisDeliveryFee findBySessionId(String sessionId);
}
