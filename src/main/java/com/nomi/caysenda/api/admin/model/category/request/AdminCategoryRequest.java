package com.nomi.caysenda.api.admin.model.category.request;

public class AdminCategoryRequest {
    Integer id;
    String name;
    String sku;
    String slug;
    Boolean active;
    Long rate;
    Float factorDefault;
    Float factor1;
    Float factor2;
    Float factor3;
    Float factor4;
    Integer parent;
    String thumbnail;
    String banner;
    Long conditionPurchse;
    Boolean updateProduct;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUpdateProduct() {
        return updateProduct;
    }

    public void setUpdateProduct(Boolean updateProduct) {
        this.updateProduct = updateProduct;
    }

    public Long getConditionPurchse() {
        return conditionPurchse;
    }

    public void setConditionPurchse(Long conditionPurchse) {
        this.conditionPurchse = conditionPurchse;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Float getFactorDefault() {
        return factorDefault;
    }

    public void setFactorDefault(Float factorDefault) {
        this.factorDefault = factorDefault;
    }

    public Float getFactor1() {
        return factor1;
    }

    public void setFactor1(Float factor1) {
        this.factor1 = factor1;
    }

    public Float getFactor2() {
        return factor2;
    }

    public void setFactor2(Float factor2) {
        this.factor2 = factor2;
    }

    public Float getFactor3() {
        return factor3;
    }

    public void setFactor3(Float factor3) {
        this.factor3 = factor3;
    }

    public Float getFactor4() {
        return factor4;
    }

    public void setFactor4(Float factor4) {
        this.factor4 = factor4;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}
