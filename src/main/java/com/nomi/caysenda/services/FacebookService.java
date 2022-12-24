package com.nomi.caysenda.services;

import com.nomi.caysenda.facebook.entity.FacebookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacebookService {
    Page<FacebookEntity> findAllUploaded(Pageable pageable);
    void createPostByCat(Integer catId);
    Long countUploaded(Integer catID);
    void deletePostByCat(Integer catId);
    void cancelRunning();
}
