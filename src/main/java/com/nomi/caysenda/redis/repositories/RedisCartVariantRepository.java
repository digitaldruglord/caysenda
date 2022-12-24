package com.nomi.caysenda.redis.repositories;

import com.nomi.caysenda.redis.model.RedisCartVariant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisCartVariantRepository extends CrudRepository<RedisCartVariant,Long> {
    RedisCartVariant findByProductCartIdAndVariantId(Long productCartId,Integer variantId);
    List<RedisCartVariant> findAllByProductCartId(Long productCartId);
    List<RedisCartVariant> findAllByProductCartIdAndActive(Long productCartId,Boolean active);
    void deleteAllByProductCartId(Long productId);
}
