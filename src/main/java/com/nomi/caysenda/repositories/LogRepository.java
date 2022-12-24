package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity,Integer> {
    void deleteAllByType(String type);
}
