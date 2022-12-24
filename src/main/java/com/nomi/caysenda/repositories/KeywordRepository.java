package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.KeywordStatictisDTO;
import com.nomi.caysenda.entity.KeywordEntity;
import com.nomi.caysenda.repositories.custom.KeywordCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface KeywordRepository extends JpaRepository<KeywordEntity,Integer>, KeywordCustomRepository {
    @Query("SELECT new com.nomi.caysenda.dto.KeywordStatictisDTO(k.keyword,COUNT (k.keyword)) FROM KeywordEntity k WHERE k.createDate BETWEEN :from AND :to GROUP BY k.keyword ORDER BY COUNT (k.keyword) DESC ")
    Page<KeywordStatictisDTO> statictis(@Param("from")Date date,
                                        @Param("to") Date to,
                                        Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.KeywordStatictisDTO(k.keyword,COUNT (k.keyword)) FROM KeywordEntity k  GROUP BY k.keyword ORDER BY COUNT (k.keyword) DESC ")
    Page<KeywordStatictisDTO> statictis(Pageable pageable);
}
