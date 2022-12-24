package com.nomi.caysenda.ghn.repositories;

import com.nomi.caysenda.ghn.entity.GHNSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GhnSettingRepository extends JpaRepository<GHNSettingEntity,Integer> {
    GHNSettingEntity findByKeySetting(String key);
    void deleteByKeySetting(String key);
}
