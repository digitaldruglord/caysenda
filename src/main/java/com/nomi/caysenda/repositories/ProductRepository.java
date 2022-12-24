package com.nomi.caysenda.repositories;

import com.nomi.caysenda.api.admin.model.product.response.AdminProductDTO;

import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.dto.ProductGallery;
import com.nomi.caysenda.dto.ProductSEO;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.repositories.custom.ProductCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer>, ProductCustomRepository {
   <T> T findBySkuZh(String sku, Class<T> type);
   List<ProductEntity> findAllByLink(String link);
   boolean existsBySlugVi(String slugVi);
   boolean existsBySkuVi(String skuVi);
   boolean existsBySkuZh(String skuZh);
   ProductEntity findBySkuVi(String sku);
   @Query("SELECT new com.nomi.caysenda.dto.ProductSEO(p.id,p.nameVi,p.description,p.thumbnail,MIN(v.vip4),p.slugVi,p.keyword) FROM ProductEntity p LEFT JOIN p.variants v WHERE p.slugVi=:slug")
   ProductSEO findBySlugForProductSEO(@Param("slug") String slug);
   @Query("SELECT p FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c WHERE c.id=?1 GROUP BY p.id")
   List<ProductEntity> findAllByCategory(Integer categoryId);
   List<ProductEntity> findAllByCategoryDefault(Integer categoryId);
   @Query("SELECT new com.nomi.caysenda.api.admin.model.product.response.AdminProductDTO(p.id,p.nameVi,p.nameZh,p.thumbnail,p.skuVi,p.skuZh,min (v.price),max(v.price),sum(v.stock),p.link,p.slugVi,c.slug,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c WHERE c.id=p.categoryDefault GROUP BY p.id")
   Page<AdminProductDTO> customFindAllForAdminProductDTO(Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.api.admin.model.product.response.AdminProductDTO(p.id,p.nameVi,p.nameZh,p.thumbnail,p.skuVi,p.skuZh,min (v.price),max(v.price),sum(v.stock),p.link,p.slugVi,c.slug,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v  LEFT JOIN p.categories c WHERE (p.nameVi like :keyword OR p.nameZh like :keyword) AND c.id=p.categoryDefault GROUP BY p.id")
   Page<AdminProductDTO> customFindAllForAdminProductDTO(String keyword,Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.api.admin.model.product.response.AdminProductDTO(p.id,p.nameVi,p.nameZh,p.thumbnail,p.skuVi,p.skuZh,min (v.price),max(v.price),sum(v.stock),p.link,p.slugVi,c.slug,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c WHERE (p.nameVi like :keyword OR p.nameZh like :keyword) AND c.id=:catId AND c.id=p.categoryDefault GROUP BY p.id")
   Page<AdminProductDTO> customFindAllForAdminProductDTO(@Param("keyword") String keyword,
                                                         @Param("catId") Integer catId, Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE p.categoryDefault=c.id AND provider.host=:host GROUP BY p.id")
   List<ProductDTO> findAllForProductDTO(@Param("host") String host);

   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE p.categoryDefault=c.id AND provider.host=:host GROUP BY p.id")
   Page<ProductDTO> findAllForProductDTO(@Param("host") String host, Pageable pageable);


   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE (p.categoryDefault=c.id AND provider.host=:host AND p.nameVi LIKE :keyword) OR (p.categoryDefault=c.id AND provider.host=:host AND p.skuVi LIKE :keyword) GROUP BY p.id")
   Page<ProductDTO> findAllForProductDTO(@Param("keyword") String keyword,
                                         @Param("host") String host,Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE (p.categoryDefault=c.id AND provider.host=:host AND p.nameVi LIKE :keyword AND c.slug=:catSlug) OR (p.categoryDefault=c.id AND provider.host=:host AND  p.skuVi LIKE :keyword AND c.slug=:catSlug) GROUP BY p.id")
   Page<ProductDTO> findAllForProductDTO(@Param("keyword")String keyword,
                                         @Param("catSlug")String catSlug,
                                         @Param("host") String host,Pageable pageable);


   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE c.id=p.categoryDefault AND provider.host=:host GROUP BY (p.id) ORDER BY RAND()")
   Page<ProductDTO> findRandForProductDTO(@Param("host") String host,
                                          Pageable pageable);

   @Query("SELECT new com.nomi.caysenda.dto.ProductGallery(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,p.thumbnail,c.name,c.slug,p.gallery) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE c.id=p.categoryDefault AND provider.host=:host GROUP BY (p.id) ORDER BY RAND()")
   Page<ProductGallery> findRandForProductGallery(@Param("host") String host,
                                                  Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider WHERE c.id=:categoryId AND provider.host=:host GROUP BY (p.id) ORDER BY RAND()")
   Page<ProductDTO> findByCategoryRandForProductDTO(@Param("categoryId")Integer categoryId,
                                                    @Param("host") String host,
                                                    Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c LEFT JOIN p.providers provider  WHERE c.id=:categoryId AND provider.host=:host GROUP BY (p.id)")
   Page<ProductDTO> findByCategoryForProductDTO(@Param("categoryId") Integer categoryId,
                                                @Param("host") String host,
                                                Pageable pageable);

   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c  LEFT JOIN p.providers provider WHERE c.slug=:slug AND provider.host=:host GROUP BY (p.id)")
   Page<ProductDTO> findByCategorySlugForProductDTO(@Param("slug") String slug,
                                                    @Param("host") String host,
                                                    Pageable pageable);
   @Query("SELECT new com.nomi.caysenda.dto.ProductDTO(p.id,p.nameVi,p.slugVi,p.skuVi,min (v.price) as minPrice,max(v.price) as maxPrice,min (v.vip1) as minv1,min (v.vip2) as minv2,min (v.vip3) as minv3,min (v.vip4) as minv4,p.thumbnail,c.name,c.slug,p.sold,p.quickviewGallery,p.unit,p.conditiondefault,p.condition1,p.condition2,p.condition3,p.condition4,p.topFlag) FROM ProductEntity p LEFT JOIN p.variants v LEFT JOIN p.categories c  LEFT JOIN p.providers provider WHERE p.topFlag = '1' AND provider.host=:host GROUP BY (p.id)")
   Page<ProductDTO> findByHotProductForProductDTO( @Param("host") String host,
                                                   Pageable pageable);
   ProductEntity findBySlugVi(String slug);
   @Query("SELECT p FROM ProductEntity p LEFT JOIN p.providers pr WHERE p.categoryDefault=:catId AND pr.host=:host")
   List<ProductEntity> findAllByCatAndHost(@Param("catId") Integer catId,
                                           @Param("host") String host);
   @Query("SELECT p FROM ProductEntity p WHERE p.categoryDefault=:catId")
   List<ProductEntity> findAllByCat(@Param("catId") Integer catId);

   @Query(	value = "SELECT  P.slug_vi                                             AS SLUG,\n" +
           "        P.name_vi                                             AS NAME,\n" +
           "        P.content                                             AS CONTENT,\n" +
           "        'NOMI'                                                AS SUPPLIER,\n" +
           "        CAT.name                                              AS CATEGORY,\n" +
           "        P.keyword                                             AS TAGS,\n" +
           "        'TRUE'                                                AS DISPLAY,\n" +
           "        'Chọn Mẫu'                                            AS ATTRIBUTE1,\n" +
           "        V.name_vi                                             AS ATTRIBUTE1_VALUE,\n" +
           "        ''                                                    AS ATTRIBUTE2,\n" +
           "        ''                                                    AS ATTRIBUTE2_VALUE,\n" +
           "        ''                                                    AS ATTRIBUTE3,\n" +
           "        ''                                                    AS ATTRIBUTE3_VALUE,\n" +
           "        P.sku_vi                                              AS SKU,\n" +
           "        'bizweb'                                              AS INVENTORY,\n" +
           "        V.stock                                               AS QTY,\n" +
           "        'deny'                                                AS BUYALL,\n" +
           "        ''                                                    AS FULLFILMENT,\n" +
           "        V.price                                               AS PRICE,\n" +
           "        ''                                                    AS PRICE_COMPARE,\n" +
           "        'TRUE'                                                AS DELIVERY,\n" +
           "        'FALSE'                                               AS VAT,\n" +
           "        V.sku_vi                                              AS BARCODE,\n" +
           "        CONCAT('https://caysenda.vn/', P.thumbnail)           AS THUMBNAIL,\n" +
           "        P.name_vi                                             AS THUMBNAIL_NOTE,\n" +
           "        P.name_vi                                             AS SEO_TITLE,\n" +
           "        P.name_vi                                             AS SEO_DESCRIPTION,\n" +
           "        V.weight                                              AS WEIGHT,\n" +
           "        'g'                                                   AS WEIGHT_UNIT,\n" +
           "        CONCAT('https://caysenda.vn/', V.thumbnail)           AS VARIANT_THUMBNAIL,\n" +
           "        P.name_vi                                             AS SHORT_DESCRIPTION\n" +
           "FROM    PRODUCTS P\n" +
           "        LEFT JOIN CATEGORIES CAT\n" +
           "            ON CAT.ID = P.CATEGORY_DEFAULT\n" +
           "        LEFT JOIN VARIANTS V\n" +
           "            ON V.PRODUCTID = P.ID\n" +
           "WHERE   CAT.id  = :category" , nativeQuery = true)
   List<Map> exportSapo(@Param("category") Integer category);
}
