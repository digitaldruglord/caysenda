package com.nomi.caysenda.dto;

import com.nomi.caysenda.entity.CategoryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Integer id;
    private String name;
    private String slug;
    private String sku;
    private Long minPrice;
    private Long maxPrice;
    private Long minVip1;
    private Long minVip2;
    private Long minVip3;
    private Long minVip4;
    private String thumbnail;
    private String categoryName;
    private String categorySlug;
    private Long sold;
    List<String> gallery;
    String unit;
    private Integer conditionDefault;
    private Integer condition1;
    private Integer condition2;
    private Integer condition3;
    private Integer condition4;
    private  String topFlag;
    public ProductDTO() {
    }


    public ProductDTO(Integer id, String name, String slug, String sku, Long minPrice, Long maxPrice, String thumbnail, String categoryName, String categorySlug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
    }

    public ProductDTO(Integer id, String name, String slug, String sku, Long minPrice, Long maxPrice, Long minVip1, Long minVip2, Long minVip3, Long minVip4, String thumbnail, String categoryName, String categorySlug, Long sold, List<String> gallery, String unit) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minVip1 = minVip1;
        this.minVip2 = minVip2;
        this.minVip3 = minVip3;
        this.minVip4 = minVip4;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.sold = sold;
        this.gallery = gallery;
        this.unit = unit;
    }

    public ProductDTO(Integer id, String name, String slug, String sku, Long minPrice, Long maxPrice, Long minVip1, Long minVip2, Long minVip3, Long minVip4, String thumbnail, String categoryName, String categorySlug, Long sold, List<String> gallery, String unit, Integer conditionDefault, Integer condition1, Integer condition2, Integer condition3, Integer condition4) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minVip1 = minVip1;
        this.minVip2 = minVip2;
        this.minVip3 = minVip3;
        this.minVip4 = minVip4;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.sold = sold;
        this.gallery = gallery;
        this.unit = unit;
        this.conditionDefault = conditionDefault;
        this.condition1 = condition1;
        this.condition2 = condition2;
        this.condition3 = condition3;
        this.condition4 = condition4;
    }

    public ProductDTO(Integer id, String name, String slug, String sku, Long minPrice, Long maxPrice, Long minVip1, Long minVip2, Long minVip3, Long minVip4, String thumbnail, String categoryName, String categorySlug, Long sold, List<String> gallery, String unit, Integer conditionDefault, Integer condition1, Integer condition2, Integer condition3, Integer condition4, String topFlag) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sku = sku;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minVip1 = minVip1;
        this.minVip2 = minVip2;
        this.minVip3 = minVip3;
        this.minVip4 = minVip4;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.categorySlug = categorySlug;
        this.sold = sold;
        this.gallery = gallery;
        this.unit = unit;
        this.conditionDefault = conditionDefault;
        this.condition1 = condition1;
        this.condition2 = condition2;
        this.condition3 = condition3;
        this.condition4 = condition4;
        this.topFlag = topFlag;
    }
}
