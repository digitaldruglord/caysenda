package com.nomi.caysenda.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nomi.caysenda.api.admin.model.product.request.AdminProductRequest;
import com.nomi.caysenda.dto.ProductAndVariantDTO;
import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.dto.ProductGallery;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.exceptions.product.ProductException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<ProductEntity> findAllByIds(List<Integer> ids);
    ProductEntity findBySlugVi(String slug);
    ProductEntity findBySkuVi(String sku);
    void delete(Integer id);
    void deleteByIds(List<Integer> ids);
    ProductEntity findById(Integer id);
    Page findAll(Pageable pageable);
    Page findAll(String keyword,Pageable pageable);
    Page findAll(String keyword,Integer catId,Pageable pageable);
    Page findByKeyword(String keyword);
    Page findByKeywordAndCat(String keyword);
    ProductEntity create(AdminProductRequest productRequest) throws ProductException;
    ProductEntity update(AdminProductRequest productRequest);
    void update(ProductEntity productEntity);
    void updateTopFlag(Integer id, String flag);
    ProductEntity createAdmin(AdminProductRequest productRequest) throws ProductException;
    ProductEntity updateAdmin(AdminProductRequest productRequest);
    Page findAllForProductDTO(Pageable pageable);
    Page<ProductDTO> findAllForProductDTO(String keyword,Pageable pageable);
    Page<ProductDTO> findAllForProductDTO(String keyword,String catSlug,Pageable pageable);
    Page<ProductDTO> findRandForProductDTO(Pageable pageable);
    void reduceStock(Integer variantId,Integer quantity);
    void increaseStock(Integer variantId,Integer quantity);
    Page<ProductGallery> findRandForProductGallery(Pageable pageable);
    Page<ProductDTO> findByCategoryRandForProductDTO(Integer categoryId,Pageable pageable);
    Page<ProductDTO> findByCategoryForProductDTO(Integer categoryId,Pageable pageable);
    Page<ProductDTO> findByCategorySlugForProductDTO(String slug,Pageable pageable);
    Page<ProductDTO> findByHotProductForProductDTO(Pageable pageable);
    Page<ProductAndVariantDTO> findAllForProductAndVariantDTOByKeyword(String keyword, Pageable pageable);
    Page<ProductAndVariantDTO> findAllForProductAndVariantDTO(Pageable pageable);
    void importExcel(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException;
    void downloadThumbnail(Integer categoryId) throws IOException;
    void downloadThumbnailAddText(Integer categoryId) throws IOException;
    void downloadImagesAddText(Integer categoryId) throws IOException;
    void downloadImages(Integer categoryId) throws IOException;
    void onChangeUpdateCategory(Integer categoryId);
    String getQuickview(Integer id) throws JsonProcessingException;
    void deleteAllByCat(Integer catId);
    void generatePriceQuoteExcel(Integer category) throws IOException;
    void generatePriceQuoteExcelWithThumbnail(Integer category) throws Exception;
    void updateCategory(Integer categoryId);
    @Cacheable(value = "productscache")
    List<ProductDTO> getProductFromCache(Integer page,Integer pageSize);
    @CacheEvict(value = "productscache",allEntries = true)
    void removeproductFromCache();
    Page<ProductDTO> search(String keyword,String slug,Pageable pageable);
    List<String> findAllKeywordByKeyword(String keyword,String host);
    List<ProductEntity> findAllByCatAndHost(Integer catId, String host);
    List<ProductEntity> findAllByCat(Integer catId);
}
