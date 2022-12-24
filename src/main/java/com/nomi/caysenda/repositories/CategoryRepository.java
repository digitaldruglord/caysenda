package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.CategoryCountDTO;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.repositories.custom.CategoryCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<CategoryEntity,Integer>, CategoryCustomRepository {
    CategoryEntity findBySku(String sku);
    Optional<CategoryEntity> findBySlug(String slug);
    List<CategoryEntity> findAllByProducts(ProductEntity productEntity);
    @Query("SELECT c FROM CategoryEntity  c LEFT JOIN c.categoryProvider pv  WHERE c.parent=:parent AND pv.host=:host")
    List<CategoryEntity> findByParent( @Param("parent") Integer parent, @Param("host") String host);
    @Query("SELECT new com.nomi.caysenda.dto.CategoryDTO(c.id,c.name,c.slug,c.thumbnail,c.banner,c.parent) FROM CategoryEntity c")
    List<CategoryDTO> findAllForCategoryDTO();
    Page<CategoryEntity> findAllByNameLike(String name, Pageable pageable);
    @Query("SELECT new com.nomi.caysenda.dto.CategoryDTO(c.id,c.name,c.slug,c.thumbnail,c.banner,c.parent,COUNT(p)) FROM CategoryEntity c LEFT JOIN c.products p LEFT JOIN c.categoryProvider pv WHERE pv.host=:host GROUP BY c")
    List<CategoryDTO> findAllByDomainForCategoryDTO(@Param("host") String host);
    @Query("SELECT new com.nomi.caysenda.dto.CategoryDTO(c.id,c.name,c.slug,c.thumbnail,c.banner,c.parent,COUNT(p)) FROM CategoryEntity c LEFT JOIN c.products p LEFT JOIN c.categoryProvider pv WHERE pv.host=:host GROUP BY c")
    Page<CategoryDTO> findAllByDomainForCategoryDTO(@Param("host") String host,Pageable pageable);
    @Query("SELECT CASE WHEN COUNT (c)>0 THEN TRUE ELSE FALSE END FROM CategoryEntity c LEFT JOIN c.categoryProvider pv WHERE c.slug=:slug AND pv.host=:host")
    Boolean existsBySlugAndDomain(@Param("slug") String slug,
                                  @Param("host") String host);
}
