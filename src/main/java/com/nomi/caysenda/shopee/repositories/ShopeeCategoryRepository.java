package com.nomi.caysenda.shopee.repositories;

import com.nomi.caysenda.shopee.entities.ShopeeCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopeeCategoryRepository extends JpaRepository<ShopeeCategoryEntity,Integer> {
    Page<ShopeeCategoryEntity> findAllByNameLike(String name, Pageable pageable);
}
