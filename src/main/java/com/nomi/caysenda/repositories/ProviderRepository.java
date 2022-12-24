package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProviderRepository extends JpaRepository<ProviderEntity,Integer> {
    ProviderEntity findByHost(String host);
    @Query("FROM ProviderEntity pv LEFT JOIN pv.optionEntities o WHERE o.id=?1")
    List<ProviderEntity> findAllByOptionId(Integer optionId);
    Boolean existsByHost(String host);

}
