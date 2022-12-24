package com.nomi.caysenda.api.admin.impl;


import com.nomi.caysenda.api.admin.AdminProductAPI;
import com.nomi.caysenda.api.admin.model.product.request.AdminProductRequest;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.exceptions.product.ProductException;
import com.nomi.caysenda.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductAPIImpl implements AdminProductAPI {
    @Autowired ProductService productService;
    @Autowired CategoryService categoryService;
    @Autowired ImageService imageService;
    @Autowired ProgressService progressService;
    @Override
    public ResponseEntity<Map> findAll(Integer page, Integer pageSize, String keyword, Integer catId) {
        Map map = new HashMap();
        map.put("success",true);
        Pageable pageable = PageRequest.of(page==null?0:page,pageSize==null?20:pageSize, Sort.by(Sort.Direction.DESC,"id"));
        if (keyword!=null && (catId==null || catId.equals(0))){
            map.put("data",productService.findAll("%"+keyword+"%",pageable));
        }else if (keyword!=null && catId!=null ){
            map.put("data",productService.findAll("%"+keyword+"%",catId,pageable));
        } else {
            map.put("data",productService.findAll(pageable));
        }

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> searchProductVariant(Integer page, Integer pageSize, String keyword) {
        Map map = new HashMap();
        map.put("success",true);
        if (keyword!=null){
            map.put("data",productService.findAllForProductAndVariantDTOByKeyword(keyword,PageRequest.of(page==null?0:page,pageSize==null?20:pageSize,Sort.by(Sort.Direction.DESC,"id"))));
        }else {
            map.put("data",productService.findAllForProductAndVariantDTO(PageRequest.of(page==null?0:page,pageSize==null?20:pageSize,Sort.by(Sort.Direction.DESC,"id"))));
        }

        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> create(AdminProductRequest productRequest) throws ProductException {
        Map map = new HashMap();
        ProductEntity productEntity;
        if (productRequest.getId()!=null){
            productEntity = productService.updateAdmin(productRequest);
        }else {
            productEntity = productService.createAdmin(productRequest);
            map.put("id",productEntity.getId());
        }
        if (productEntity!=null){
            map.put("success",true);
        }else {
            map.put("success",false);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> delete(Integer id) {
        productService.delete(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> findById(Integer id) {
        Map map = new HashMap();
        ProductEntity productEntity = productService.findById(id);
        if (productEntity!=null){
            map.put("success",true);
            map.put("data",productEntity);
        }else {
            map.put("success",false);
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> importExcel(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
        productService.importExcel(multipartHttpServletRequest);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> generateImageZip(Integer categoryId, String action) {
        if (categoryId!=null){
            try {
                productService.downloadThumbnail(categoryId);
                productService.downloadImages(categoryId);
                productService.downloadImagesAddText(categoryId);
                productService.downloadThumbnailAddText(categoryId);
            } catch (IOException e) {

            }
        }else {
            List<CategoryEntity> categories = categoryService.findAll();
            AtomicReference<Integer> index = new AtomicReference<>(0);
            progressService.save(0,100,"PROGRESS_IMAGE_ZIP");
            categories.forEach(categoryEntity -> {
                progressService.save(index.getAndSet(index.get() + 1),categories.size(),"PROGRESS_IMAGE_ZIP");
                try {
                    productService.downloadThumbnail(categoryEntity.getId());
                    productService.downloadImages(categoryEntity.getId());
                    productService.downloadImagesAddText(categoryEntity.getId());
                    productService.downloadThumbnailAddText(categoryEntity.getId());
                } catch (IOException e) {

                }
            });
            progressService.delete("PROGRESS_IMAGE_ZIP");
        }

        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteByCat(Integer categoryId) {
        Map map = new HashMap();
        productService.deleteAllByCat(categoryId);
        return ResponseEntity.ok(map);
    }
    @Autowired
    SettingService settingService;
    @Override
    public ResponseEntity<Map> testCache(Integer id) {
        Map map = new HashMap();
        settingService.getEmbedHeader();
        settingService.getPriceSetting();
        settingService.getEmbedSocial();
        settingService.getWebsite();
        map.put("data", settingService.getPriceSetting());
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Map> updateTopFlag(Integer id, String flag) {
        productService.updateTopFlag(id,flag);
		return ResponseEntity.ok(Map.of("success",true));
	}
}
