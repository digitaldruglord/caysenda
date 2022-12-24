package com.nomi.caysenda.redis.services.impl;

import com.nomi.caysenda.redis.model.RedisRunning;
import com.nomi.caysenda.redis.repositories.RedisRunningRepository;
import com.nomi.caysenda.redis.services.RedisRunningService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class RedisRunningServiceImpl implements RedisRunningService {
    @Autowired
    RedisRunningRepository runningRepository;
    @Autowired
    Environment env;

    @Override
    public void save(String taskcode) {

        RedisRunning redisRunning = findByTaskCode(taskcode);
        if (redisRunning==null) redisRunning = new RedisRunning();
        redisRunning.setRunning(true);
        redisRunning.setTaskCode(taskcode);
        redisRunning.setHost(env.getProperty("spring.domain"));
        runningRepository.save(redisRunning);

    }

    @Override
    public void delete(String taskCode) {

        RedisRunning redisRunning = runningRepository.findByTaskCodeAndHost(taskCode,env.getProperty("spring.domain"));
        if (redisRunning!=null){
            runningRepository.delete(redisRunning);
        }
    }

    @Override
    public RedisRunning findByTaskCode(String taskCode) {
        return runningRepository.findByTaskCodeAndHost(taskCode,env.getProperty("spring.domain"));
    }
}
