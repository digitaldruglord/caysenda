package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisCartProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RedisCartProductRepository extends CrudRepository<RedisCartProduct,Long> {
    RedisCartProduct findBySessionIdAndProductId(String sessionId,Integer productId);
    List<RedisCartProduct> findAllBySessionId(String sessionId);
    List<RedisCartProduct> findByProductId(Integer productId);
    List<RedisCartProduct> findAllByName(String name);
    List<RedisCartProduct> findAllByCategoryIdAndSessionId(Integer categoryId,String sessionId);
}
