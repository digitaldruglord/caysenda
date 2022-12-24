package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisAddressRepository extends CrudRepository<RedisAddress,Integer> {
    RedisAddress findBySessionIdAndActive(String sessionId,Boolean active);

    List<RedisAddress> findAllBySessionId(String sessionId);

}
