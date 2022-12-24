package com.nomi.caysenda.extension.repositories;

import com.nomi.caysenda.entity.DictionaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DictionaryRepository extends JpaRepository<DictionaryEntity,Integer> {
    DictionaryEntity findByZhWord(String zhWord);
    Page<DictionaryEntity> findAllByZhWordLikeOrViWordLike(String zhWord, String viWord, Pageable pageable);
}
