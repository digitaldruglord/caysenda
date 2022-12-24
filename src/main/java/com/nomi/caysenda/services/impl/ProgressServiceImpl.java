package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.redis.model.RedisProgress;
import com.nomi.caysenda.redis.repositories.RedisProgressRepository;
import com.nomi.caysenda.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.text.DecimalFormat;

@Service
public class ProgressServiceImpl implements ProgressService {
    @Autowired RedisProgressRepository progressRepository;
    @Autowired Environment env;
    @Override
    public void save(Integer current, Integer all, String code) {
        RedisProgress redisProgress = progressRepository.findByTaskCodeAndHost(code,env.getProperty("spring.domain"));
        if (redisProgress==null) redisProgress = new RedisProgress();
        Float process = Float.valueOf(current.floatValue()/all.floatValue());
        DecimalFormat df = new DecimalFormat("#.##");
        redisProgress.setProgress(Float.valueOf(df.format(process)));
        redisProgress.setTaskCode(code);
        redisProgress.setCurrent(current);
        redisProgress.setSize(all);
        redisProgress.setHost(env.getProperty("spring.domain"));
        progressRepository.save(redisProgress);
    }

    @Override
    public void save(Integer current, Integer all, String code, Boolean running) {
        RedisProgress redisProgress = progressRepository.findByTaskCodeAndHost(code,env.getProperty("spring.domain"));
        if (redisProgress==null) redisProgress = new RedisProgress();
        Float process = Float.valueOf(current.floatValue()/all.floatValue());
        DecimalFormat df = new DecimalFormat("#.##");
        redisProgress.setProgress(Float.valueOf(df.format(process)));
        redisProgress.setTaskCode(code);
        redisProgress.setCurrent(current);
        redisProgress.setSize(all);
        redisProgress.setRunning(running);
        redisProgress.setHost(env.getProperty("spring.domain"));
        progressRepository.save(redisProgress);

    }

    @Override
    public void delete(String code) {
        RedisProgress redisProgress = progressRepository.findByTaskCodeAndHost(code,env.getProperty("spring.domain"));
        if (redisProgress!=null){
            progressRepository.delete(redisProgress);
        }

    }

    @Override
    public RedisProgress findByCode(String code) {
        RedisProgress redisProgress = progressRepository.findByTaskCodeAndHost(code,env.getProperty("spring.domain"));
        return redisProgress;
    }
}
