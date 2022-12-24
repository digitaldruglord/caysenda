package com.nomi.caysenda.repositories.custom;

import com.nomi.caysenda.dto.CategoryCountDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryCustomRepository {
    List<CategoryCountDTO> findAllAndCount();
    Long countAllCriteria(String name, Integer host);
    Page<CategoryEntity> findAllCriteria(String name, Integer host, Pageable pageable);
}
