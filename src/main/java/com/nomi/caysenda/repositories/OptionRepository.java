package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OptionRepository extends JpaRepository<OptionEntity,Integer> {

    @Query("FROM OptionEntity o LEFT JOIN o.providerOption p WHERE o.code=:code AND p.host=:host")
    OptionEntity findByCode(@Param("code") String optionKey,@Param("host") String host);
    void deleteByCode(String optionKey);
}
