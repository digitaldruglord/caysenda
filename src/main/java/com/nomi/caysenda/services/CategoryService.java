package com.nomi.caysenda.services;

import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateDomainRequest;
import com.nomi.caysenda.dto.CategoryCountDTO;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.exceptions.category.CategoryException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<CategoryEntity> findById(Integer id);
    Optional<CategoryEntity> findBySlug(String slug);
    List<CategoryEntity> findAll();
    Page<CategoryEntity> findAll(Pageable pageable);
    List<CategoryDTO> findAllByDomainForCategoryDTO();
    Page<CategoryDTO> findAllDomainForCategoryDTO(Pageable pageable);
    Page<CategoryEntity> findAllByNameLike(String name,Pageable pageable);
    Page<CategoryEntity> findByCriteria(String name,Integer host,Pageable pageable);
    List<CategoryDTO> findAllForCategoryDTO();
    CategoryEntity create(AdminCategoryRequest categoryRequest) throws CategoryException;
    CategoryEntity update(AdminCategoryRequest categoryRequest);
    void delete(Integer id) throws CategoryException;
    List<CategoryCountDTO> findAllAndCount();
    List<CategoryEntity> getAllParent(Integer id);
    List<CategoryEntity> getAllParent(String slug);
    void updateDomain(AdminCategoryUpdateDomainRequest request);
    Boolean isExistsBySlugAndDomain(String slug);
    List<CategoryEntity> findAllByParent(Integer id);
    void generateLazadaExcel();


}
