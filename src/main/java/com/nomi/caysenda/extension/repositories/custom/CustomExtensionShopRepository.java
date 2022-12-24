package com.nomi.caysenda.extension.repositories.custom;

import com.nomi.caysenda.extension.dto.ShopExtensionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomExtensionShopRepository {
    Object countAllCriteria(String keyword, Integer userId);
    Page<ShopExtensionDTO> findAllCriteria(String keyword, Integer userId, Pageable pageable);
}
