package com.nomi.caysenda.repositories;

import com.nomi.caysenda.entity.PageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageRepository extends JpaRepository<PageEntity,Integer> {
    PageEntity findBySlug(String slug);
    @Query("SELECT p FROM PageEntity p LEFT JOIN p.providerPage provider WHERE provider.host=:host")
    Page<PageEntity> findAll(@Param("host")String host, Pageable pageable);
    @Query("SELECT p FROM PageEntity p LEFT JOIN p.providerPage provider WHERE provider.host=:host")
    List<PageEntity> findAll(@Param("host")String host);
    @Query("SELECT p FROM PageEntity p LEFT JOIN p.providerPage provider WHERE provider.host=:host AND p.type=:type")
    Page<PageEntity> findAll(@Param("type") String type,
                             @Param("host")String host,
                             Pageable pageable);
    @Query("SELECT p FROM PageEntity p LEFT JOIN p.providerPage provider WHERE p.slug=:slug AND provider.host=:host")
    PageEntity findBySlugAndHost(@Param("slug")String slug,@Param("host")String host);
    @Query("SELECT p FROM PageEntity p LEFT JOIN p.providerPage provider WHERE p.id=:id AND provider.host=:host")
    PageEntity findByIdAndHost(@Param("id")Integer id,@Param("host")String host);
    @Query("SELECT CASE WHEN count (p)> 0 THEN true ELSE false END FROM PageEntity p LEFT JOIN p.providerPage provider WHERE p.slug=:slug AND provider.host=:host")
    Boolean existsBySlugAndHost(@Param("slug") String slug,
                                @Param("host") String host);
}
