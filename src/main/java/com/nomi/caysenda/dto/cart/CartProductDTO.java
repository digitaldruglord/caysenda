package com.nomi.caysenda.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CartProductDTO {
    Long id;
    Integer productId;
    String name;
    String sku;
    String slug;
    Integer conditionDefault;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    Integer condition4;
    Long priceDefault;
    Long price1;
    Long price2;
    Long price3;
    Long price4;
    String thumbnail;
    Integer categoryId;
    Boolean retail;
    List<CartVariantDTO> variants;
    Integer quantity;
    Integer quantityActive;
    Integer range;
    Long amount;
    Long amountActive;
    Boolean active;
    Double weight;
    String linkZh;
    String nameZh;
    String unit;
    String topFlag;


    public CartProductDTO() {
    }

    public CartProductDTO(Long id, Integer productId, String name, String sku, String slug, Integer conditionDefault, Integer condition1, Integer condition2, Integer condition3, Integer condition4, String thumbnail, Integer categoryId, Boolean retail, String linkZh, String nameZh, String unit, String topFlag) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.slug = slug;
        this.conditionDefault = conditionDefault;
        this.condition1 = condition1;
        this.condition2 = condition2;
        this.condition3 = condition3;
        this.condition4 = condition4;
        this.thumbnail = thumbnail;
        this.categoryId = categoryId;
        this.retail = retail;
        this.linkZh = linkZh;
        this.nameZh = nameZh;
        this.unit = unit;
        this.topFlag = topFlag;
    }

}
