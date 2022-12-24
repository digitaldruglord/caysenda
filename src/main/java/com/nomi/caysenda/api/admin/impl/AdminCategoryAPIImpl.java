package com.nomi.caysenda.api.admin.impl;


import com.nomi.caysenda.api.admin.AdminCategoryAPI;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateDomainRequest;
import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryUpdateSortRequest;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.exceptions.category.CategoryException;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.ImageService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.shopee.entities.ShopeeCategoryEntity;
import com.nomi.caysenda.shopee.services.ShopeeService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryAPIImpl implements AdminCategoryAPI {
    @Autowired CategoryService categoryService;
    @Autowired ImageService imageService;
    @Autowired ProductService productService;
    @Autowired Environment env;
    @Autowired ProviderRepository providerRepository;
    @Autowired ShopeeService shopeeService;
    @Override
    public ResponseEntity<Map> findAll(Integer page, Integer pageSize, String keyword, Integer host) {
        Map map = new HashMap();
        map.put("success",true);
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?10:pageSize, Sort.by(Sort.Direction.DESC,"id"));
        map.put("data",categoryService.findByCriteria(keyword,host,pageable));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> findAll(Integer page, Integer pageSize, String keyword) {
        Map map = new HashMap();
        map.put("success",true);
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?10:pageSize, Sort.by(Sort.Direction.DESC,"id"));
        ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
        map.put("data",categoryService.findByCriteria(keyword,providerEntity.getId(),pageable));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> create(AdminCategoryRequest categoryRequest) throws CategoryException {
        Map map = new HashMap();
        if (categoryRequest.getId()!=null){
            CategoryEntity categoryEntity = categoryService.update(categoryRequest);
            map.put("success",true);
            map.put("method","update");
            map.put("data",categoryEntity);
        }else {
            CategoryEntity categoryEntity = categoryService.create(categoryRequest);
            if (categoryEntity!=null){
                map.put("success",true);
                map.put("method","create");
                map.put("data",categoryEntity);
            }else {
                map.put("success",false);
            }
        }


        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> updateSortTree(AdminCategoryUpdateSortRequest request) throws CategoryException {
        Map map = new HashMap();
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> update() {
        return null;
    }

    @Override
    public ResponseEntity<Map> delete(Integer id) throws CategoryException {
        categoryService.delete(id);
        return new ResponseEntity<>(Map.of("success",true),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> generateThumbnail(Integer categoryId) throws IOException {
        Map map = new HashMap();
        map.put("success",true);
        imageService.createZipThumbnail(categoryId);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> generateImages(Integer categoryId) throws IOException {
        Map map = new HashMap();
        imageService.createZipImages(categoryId);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateDomain(AdminCategoryUpdateDomainRequest request) {
        Map map = new HashMap();
        categoryService.updateDomain(request);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> priceQuote(Integer category) {
        Map map = new HashMap();
        map.put("success",true);
        if (category!=null){
            try {
                productService.generatePriceQuoteExcel(category);
                productService.generatePriceQuoteExcelWithThumbnail(category);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            List<CategoryEntity> list = categoryService.findAll();
            list.forEach(categoryEntity -> {
                try {
                    productService.generatePriceQuoteExcel(categoryEntity.getId());
                    productService.generatePriceQuoteExcelWithThumbnail(categoryEntity.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> generateShopeeTemplate(Integer catId, Integer shopeeCat) throws IOException, InvalidFormatException {
        shopeeService.generateShopeeExcel(catId, shopeeCat);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getShopeeCategory(Integer page, Integer pageSize, String keyword) throws IOException, InvalidFormatException {
        Page<ShopeeCategoryEntity> data = shopeeService.findALlByName(keyword,PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:10,Sort.by(Sort.Direction.DESC,"id")));
        return ResponseEntity.ok(Map.of("success",true,"data",data));
    }

    @Override
    public ResponseEntity<Map> importShopeeCategory() throws IOException, InvalidFormatException {
        shopeeService.importShopeeCategory();
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> generateLazadaTemplate(Integer catId) throws IOException, InvalidFormatException {

        return ResponseEntity.ok(Map.of("success",true));
    }
}
