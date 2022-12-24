package com.nomi.caysenda.api.admin.model.product.request;

import com.nomi.caysenda.entity.GroupVariantEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AdminProductRequest {
    Integer id;
    String nameZh;
    String skuZh;
    String nameVi;
    String slugVi;
    String skuVi;
    String thumbnail;
    Integer status;
    String content;
    Boolean trash;
    Integer conditiondefault;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    Integer condition4;
    Integer categoryDefault;
    List<AdminProductVariantRequest> variants;
    List<Integer> categories;
    List<String> gallery;
    Boolean enableAutoUpdatePrice;
    String link;
    String material;
    String description;
    String averageWeight;
    List<Integer> providers;
    String unit;
    Boolean retail;
    List<GroupVariantEntity> variantGroup;
    String video;
    String keyword;
    String topFlag;
}
