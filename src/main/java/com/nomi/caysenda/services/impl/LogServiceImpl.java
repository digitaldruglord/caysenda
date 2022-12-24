package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.entity.LogEntity;
import com.nomi.caysenda.repositories.LogRepository;
import com.nomi.caysenda.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogRepository logRepository;
    @Override
    public void save(String log, String type) {
        LogEntity logEntity = new LogEntity();
        logEntity.setLog(log);
        logEntity.setType(type);
        logEntity.setCreated(new Date());
        logRepository.save(logEntity);
    }

    @Override
    public Page<LogEntity> findAll(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        logRepository.deleteAllByType("log_update_excel_extension");
        logRepository.deleteAllByType("log_update_web_extension");
//        List<LogEntity> list = logRepository.findAllById(ids);
//        logRepository.deleteAll(list);
    }
}
