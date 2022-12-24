package com.nomi.caysenda.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@RedisHash(value = "cartproduct",timeToLive = 86400L)
@Data
@AllArgsConstructor
@Getter
@Setter
public class RedisCartProduct {
    @Id
    Long id;
    @Indexed
    String sessionId;
    @Indexed
    Integer productId;
    @Indexed
    String name;
    String sku;
    String slug;
    Integer conditionDefault;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    Integer condition4;
    String thumbnail;
    @Indexed
    Integer categoryId;
    String typeDefault;
    String type1;
    String type2;
    String type3;
    Boolean retail;
    String linkZh;
    @Indexed
    String categorySlug;
    String nameZh;
    String unit;
    String topFlag;

    public RedisCartProduct() {
    }
}
