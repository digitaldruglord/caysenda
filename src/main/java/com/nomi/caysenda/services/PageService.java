package com.nomi.caysenda.services;

import com.nomi.caysenda.api.admin.model.post.AdminPostRequest;
import com.nomi.caysenda.entity.PageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PageService {
    List<PageEntity> findAllByDomain(String domain);
    Page<PageEntity> findAll(Pageable pageable);
    Page<PageEntity> findAll(String type,Pageable pageable);
    PageEntity findById(Integer id);
    PageEntity findBySlug(String slug);
    PageEntity save(AdminPostRequest postRequest);
    void deleteById(Integer id);
    Boolean existsBySlug(String slug);
}
