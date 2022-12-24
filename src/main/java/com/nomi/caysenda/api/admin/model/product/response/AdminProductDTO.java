package com.nomi.caysenda.api.admin.model.product.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductDTO {
    Integer id;
    String nameVi;
    String nameZh;
    String thumbnail;
    String skuVi;
    String skuZh;
    Long minPrice;
    Long maxPrice;
    Long stock;
    String link;
    String slug;
    String categorySlug;
    String topFlag;

    public AdminProductDTO() {
    }

    public AdminProductDTO(Integer id, String nameVi, String nameZh, String thumbnail, String skuVi, String skuZh, Long minPrice, Long maxPrice, Long stock, String link, String slug, String categorySlug, String topFlag) {
        this.id = id;
        this.nameVi = nameVi;
        this.nameZh = nameZh;
        this.thumbnail = thumbnail;
        this.skuVi = skuVi;
        this.skuZh = skuZh;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.stock = stock;
        this.link = link;
        this.slug = slug;
        this.categorySlug = categorySlug;
        this.topFlag = topFlag;
    }

    public AdminProductDTO(Integer id, String nameVi, String nameZh, String thumbnail, String skuVi, String skuZh, Long minPrice, Long maxPrice, Long stock, String link, String slug, String categorySlug) {
        this.id = id;
        this.nameVi = nameVi;
        this.nameZh = nameZh;
        this.thumbnail = thumbnail;
        this.skuVi = skuVi;
        this.skuZh = skuZh;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.stock = stock;
        this.link = link;
        this.slug = slug;
        this.categorySlug = categorySlug;
    }


}
