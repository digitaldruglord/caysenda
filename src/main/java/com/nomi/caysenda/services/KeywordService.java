package com.nomi.caysenda.services;

import com.nomi.caysenda.dto.KeywordStatictisDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface KeywordService {
    List<String> findAllByKeyword(String keyword);
    @Cacheable(value = "topkeyword")
    List<String> findTopKeyword();

    @CacheEvict(value = "topkeyword",allEntries = true)
    void reloadTopKeyword();

    void save(String keyword);
    Page<KeywordStatictisDTO> statictis(Integer month, Integer year,Integer page,Integer pageSize);
}
