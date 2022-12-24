package com.nomi.caysenda.services;

import com.nomi.caysenda.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LogService {
    void save(String log,String type);
    Page<LogEntity> findAll(Pageable pageable);
    void deleteByIds(List<Integer> ids);
}
