package com.nomi.caysenda.services;

import com.nomi.caysenda.entity.DictionaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface DictionaryService {
    Page<DictionaryEntity> findAll(Pageable pageable);
    DictionaryEntity findById(Integer id);
    DictionaryEntity findByZhWord(String zhWord);
    Page<DictionaryEntity> findAllByZhWordAndViWord(String search,Pageable pageable);
    DictionaryEntity createAndupdate(Integer id,String zhWord,String viWord);
    void deleteById(Integer id);
    void deleteByZhWord(String zhWord);
    void uploadExcel(MultipartHttpServletRequest request);

}
