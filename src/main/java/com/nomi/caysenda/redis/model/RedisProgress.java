package com.nomi.caysenda.redis.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;



@RedisHash(timeToLive = 86400L)
@Getter
@Setter
public class RedisProgress {
    @Id
    String taskId;
    @Indexed
    String taskCode;
    @Indexed
    String host;
    Float progress;
    Integer current;
    Integer size;
    Boolean running;


}
