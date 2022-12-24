package com.nomi.caysenda.facebook.repositories;

import com.nomi.caysenda.facebook.entity.FacebookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;
import java.util.List;


public interface FacebookRepository extends JpaRepository<FacebookEntity,Integer> {
    FacebookEntity findByProductId(Integer productId);
    Boolean existsByProductIdAndHost(Integer productId,String host);
    Long countByCateId(Integer catId);
    Page<FacebookEntity> findAllByHost(String host, Pageable pageable);
    List<FacebookEntity> findAllByCateIdAndHost(Integer catId,String host);

}
