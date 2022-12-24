package com.nomi.caysenda.repositories.custom;

import com.nomi.caysenda.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductCustomRepository {
    Long countFindAllForProductDTO(Pageable pageable);
    Page<ProductDTO> search(String keyword,String catSlug,String host, String sorter,Pageable pageable);
    List<String> findAllKeywordsByKeyword(String keyword,String host);
}
