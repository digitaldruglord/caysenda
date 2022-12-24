package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateDomainRequest;
import com.nomi.caysenda.dto.CategoryCountDTO;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.exceptions.category.CategoryException;
import com.nomi.caysenda.repositories.CategoryRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.services.ProgressService;
import com.nomi.caysenda.utils.CategoryUtils;
import com.nomi.caysenda.utils.ConvertStringToUrl;
import com.nomi.caysenda.utils.UploadFileUtils;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;
    @Autowired
    Environment env;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    ProgressService progressService;
    @Override
    public Optional<CategoryEntity> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<CategoryEntity> findBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<CategoryEntity> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<CategoryDTO> findAllByDomainForCategoryDTO() {
        List<CategoryDTO> categoryDTOS =  categoryRepository.findAllByDomainForCategoryDTO(env.getProperty("spring.domain"));
        for (CategoryDTO categoryDTO:categoryDTOS){
            categoryDTO.setSlug(getFullSlug(categoryDTO.getSlug()));
        }
        return categoryDTOS;
    }

    @Override
    public Page<CategoryDTO> findAllDomainForCategoryDTO(Pageable pageable) {
        Page<CategoryDTO> categoryDTOS = categoryRepository.findAllByDomainForCategoryDTO(env.getProperty("spring.domain"),pageable);
        for (CategoryDTO categoryDTO:categoryDTOS.getContent()){
            categoryDTO.setSlug(getFullSlug(categoryDTO.getSlug()));
        }
        return categoryDTOS;
    }


    @Override
    public Page<CategoryEntity> findAllByNameLike(String name, Pageable pageable) {
        return categoryRepository.findAllByNameLike("%"+name+"%",pageable);
    }

    @Override
    public Page<CategoryEntity> findByCriteria(String name, Integer host, Pageable pageable) {
        Page<CategoryEntity>  categoryEntities = categoryRepository.findAllCriteria(name, host, pageable);
        List<CategoryEntity> newList = new ArrayList<>();
        Iterator<CategoryEntity> iterator = categoryEntities.getContent().listIterator();
        Long totalElements = categoryEntities.getTotalElements();
        while (iterator.hasNext()){
            CategoryEntity categoryEntity = iterator.next();
            newList.add(categoryEntity);
            if (categoryEntity.getParent()!=0 && !checkExists(categoryEntities.getContent(),categoryEntity.getParent())){
                CategoryEntity entity = categoryRepository.findById(categoryEntity.getParent()).orElse(null);
                if (entity!=null && !checkExists(newList,entity.getId())){
                    newList.add(entity);
                    totalElements++;
                }
            }
        }

        return new PageImpl<>(newList,categoryEntities.getPageable(),totalElements);
    }

    private boolean checkExists(List<CategoryEntity> list,Integer id){
        for (CategoryEntity categoryEntity:list){
            if (id.equals(categoryEntity.getId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CategoryDTO> findAllForCategoryDTO() {
        List<CategoryDTO> categoryDTOS =  categoryRepository.findAllForCategoryDTO();
        for (CategoryDTO categoryDTO:categoryDTOS){
            categoryDTO.setSlug(getFullSlug(categoryDTO.getSlug()));
        }
        return categoryDTOS;
    }
    @Override
    public CategoryEntity create(AdminCategoryRequest categoryRequest) throws CategoryException {
        CategoryEntity categoryEntity = CategoryUtils.convertAdminCategoryRequestToEntity(categoryRequest);
        categoryEntity.setActive(true);
        try{
           return categoryRepository.saveAndFlush(categoryEntity);
        }catch (DataIntegrityViolationException e){
            throw new CategoryException("Slug or sku is exists",CategoryException.SKU_SLUG_EXISTS);
        }
    }
    @Override
    public CategoryEntity update(AdminCategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryRequest.getId()).get();
        if (categoryRequest.getName()!=null){
            categoryEntity.setName(categoryRequest.getName());
        }
        if (categoryRequest.getName()!=null){
            categoryEntity.setSlug(ConvertStringToUrl.covertStringToURL(categoryRequest.getName()));
        }
        if (categoryRequest.getSku()!=null){
            categoryEntity.setSku(categoryRequest.getSku());
        }
        if (categoryRequest.getActive()!=null){
            categoryEntity.setActive(categoryRequest.getActive());
        }
        if (categoryRequest.getRate()!=null){
            categoryEntity.setRate(categoryRequest.getRate());
        }
        if (categoryRequest.getFactorDefault()!=null){
            categoryEntity.setFactorDefault(categoryRequest.getFactorDefault());
        }
        if (categoryRequest.getFactor1()!=null){
            categoryEntity.setFactor1(categoryRequest.getFactor1());
        }
        if (categoryRequest.getFactor2()!=null){
            categoryEntity.setFactor2(categoryRequest.getFactor2());
        }
        if (categoryRequest.getFactor3()!=null){
            categoryEntity.setFactor3(categoryRequest.getFactor3());
        }
        if (categoryRequest.getFactor4()!=null){
            categoryEntity.setFactor4(categoryRequest.getFactor4());
        }
        if (categoryRequest.getThumbnail()!=null){
            categoryEntity.setThumbnail(categoryRequest.getThumbnail());
        }
        if (categoryRequest.getBanner()!=null){
            categoryEntity.setBanner(categoryRequest.getBanner());
        }
        if (categoryRequest.getConditionPurchse()!=null){
            categoryEntity.setConditionPurchse(categoryRequest.getConditionPurchse());
        }
        if (categoryRequest.getParent()!=null){
            categoryEntity.setParent(categoryRequest.getParent());
        }
        if (categoryRequest.getDescription()!=null){
            categoryEntity.setDescription(categoryRequest.getDescription());
        }
        if (categoryRequest.getUpdateProduct()){
            productService.onChangeUpdateCategory(categoryRequest.getId());
        }
        categoryEntity  = categoryRepository.save(categoryEntity);
        productService.updateCategory(categoryEntity.getId());
        return categoryEntity;
    }

    @Override
    public void delete(Integer id) throws CategoryException {
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(id).orElse(null);
            if (categoryEntity!=null){
                categoryRepository.delete(categoryEntity);
            }
        }catch (EmptyResultDataAccessException e){
            throw new CategoryException("not found entity",CategoryException.EMPTY_RESULT);
        }
    }

    @Override
    public List<CategoryCountDTO> findAllAndCount() {
        List<CategoryCountDTO> categoryDTOS =  categoryRepository.findAllAndCount();
        for (CategoryCountDTO categoryDTO:categoryDTOS){
            categoryDTO.setSlug(getFullSlug(categoryDTO.getSlug()));
        }
        return categoryDTOS;
    }

    @Override
    public List<CategoryEntity> getAllParent(Integer id) {
        CategoryEntity categoryEntity = findById(id).orElse(null);
        List<CategoryEntity> parents = new ArrayList<>();
        if (categoryEntity!=null){
            if (categoryEntity.getParent()!=0){
                CategoryEntity parent = findById(categoryEntity.getParent()).orElse(null);
                parents.add(parent);
                while (parent!=null && parent.getParent()!=0){
                    parent = findById(parent.getParent()).orElse(null);
                    parents.add(parent);
                }
            }
        }
        return parents;
    }

    @Override
    public List<CategoryEntity> getAllParent(String slug) {
        CategoryEntity categoryEntity = findBySlug(slug).orElse(null);
        List<CategoryEntity> parents = new ArrayList<>();
        if (categoryEntity!=null){
            if (categoryEntity.getParent()!=0){
                CategoryEntity parent = findById(categoryEntity.getParent()).orElse(null);
                parents.add(parent);
                while (parent!=null && parent.getParent()!=0){
                    parent = findById(parent.getParent()).orElse(null);
                    parents.add(parent);
                }
            }
        }
        return parents;
    }

    @Override
    public void updateDomain(AdminCategoryUpdateDomainRequest request) {
        List<ProviderEntity> providerEntities = providerRepository.findAllById(request.getDomains());
        CategoryEntity categoryEntity = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (categoryEntity!=null){
            categoryEntity.setCategoryProvider(providerEntities);
            categoryRepository.save(categoryEntity);
            List<ProductEntity> products = productRepository.findAllByCategory(categoryEntity.getId());
            taskScheduler.schedule(() -> {
                products.forEach(productEntity -> {
                    productEntity.setProviders(providerEntities);
                    productRepository.save(productEntity);
                });
            },new Date());
        }

    }

    @Override
    public Boolean isExistsBySlugAndDomain(String slug) {
        String domain = env.getProperty("spring.domain");

        return categoryRepository.existsBySlugAndDomain(slug,domain);
    }

    @Override
    public List<CategoryEntity> findAllByParent(Integer id) {
        String domain = env.getProperty("spring.domain");
        return categoryRepository.findByParent(id,domain);
    }

    @Override
    public void generateLazadaExcel() {

    }


    private String getFullSlug(String slugOrginal){
        StringBuilder builder = new StringBuilder("/");
        builder.append(slugOrginal);
        return builder.toString();
    }
}
