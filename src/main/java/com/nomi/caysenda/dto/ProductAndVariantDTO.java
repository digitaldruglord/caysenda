package com.nomi.caysenda.dto;

public class ProductAndVariantDTO {
    Integer variantId;
    Integer productId;
    Integer categoryId;
    String variantName;
    String productName;
    String variantThumbnail;
    String productThumbnail;
    Long priceDefault;
    Long price1;
    Long price2;
    Long price3;
    Long price4;
    Integer stock;
    String unit;
    String link;
    String slug;



    public ProductAndVariantDTO(Integer variantId, Integer productId, Integer categoryId, String variantName, String productName, String variantThumbnail, String productThumbnail, Long priceDefault, Long price1, Long price2, Long price3, Long price4, Integer stock, String unit, String link, String slug) {
        this.variantId = variantId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.variantName = variantName;
        this.productName = productName;
        this.variantThumbnail = variantThumbnail;
        this.productThumbnail = productThumbnail;
        this.priceDefault = priceDefault;
        this.price1 = price1;
        this.price2 = price2;
        this.price3 = price3;
        this.price4 = price4;
        this.stock = stock;
        this.unit = unit;
        this.link = link;
        this.slug = slug;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }



    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantThumbnail() {
        return variantThumbnail;
    }

    public void setVariantThumbnail(String variantThumbnail) {
        this.variantThumbnail = variantThumbnail;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public Long getPriceDefault() {
        return priceDefault;
    }

    public void setPriceDefault(Long priceDefault) {
        this.priceDefault = priceDefault;
    }

    public Long getPrice1() {
        return price1;
    }

    public void setPrice1(Long price1) {
        this.price1 = price1;
    }

    public Long getPrice2() {
        return price2;
    }

    public void setPrice2(Long price2) {
        this.price2 = price2;
    }

    public Long getPrice3() {
        return price3;
    }

    public void setPrice3(Long price3) {
        this.price3 = price3;
    }

    public Long getPrice4() {
        return price4;
    }

    public void setPrice4(Long price4) {
        this.price4 = price4;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
