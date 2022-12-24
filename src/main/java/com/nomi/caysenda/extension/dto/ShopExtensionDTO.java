package com.nomi.caysenda.extension.dto;

import com.nomi.caysenda.entity.UserEntity;

public class ShopExtensionDTO {
    Integer id;
    String name;
    String link;
    String sku;
    Long exchangeRate;
    Float factorDefault;
    Float factor1;
    Float factor2;
    Float factor3;
    Float factor4;
    String mainProduct;
    String status;
    UserEntity userExtensionShop;
    Long count;
    Boolean enableUpdatePrice;


    public ShopExtensionDTO() {
    }

    public ShopExtensionDTO(Integer id, String name, String link, String sku, Long exchangeRate, Float factorDefault, Float factor1, Float factor2, Float factor3, Float factor4, String mainProduct, String status, UserEntity userExtensionShop, Long count, Boolean enableUpdatePrice) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.sku = sku;
        this.exchangeRate = exchangeRate;
        this.factorDefault = factorDefault;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.factor3 = factor3;
        this.factor4 = factor4;
        this.mainProduct = mainProduct;
        this.status = status;
        this.userExtensionShop = userExtensionShop;
        this.count = count;
        this.enableUpdatePrice = enableUpdatePrice;
    }

    public ShopExtensionDTO(Integer id, String name, String link, String sku, Long exchangeRate, Float factorDefault, Float factor1, Float factor2, Float factor3, Float factor4, String mainProduct, String status, UserEntity userExtensionShop, Long count) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.sku = sku;
        this.exchangeRate = exchangeRate;
        this.factorDefault = factorDefault;
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.factor3 = factor3;
        this.factor4 = factor4;
        this.mainProduct = mainProduct;
        this.status = status;
        this.userExtensionShop = userExtensionShop;
        this.count = count;
    }

    public Boolean getEnableUpdatePrice() {
        return enableUpdatePrice;
    }

    public void setEnableUpdatePrice(Boolean enableUpdatePrice) {
        this.enableUpdatePrice = enableUpdatePrice;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Long exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserEntity getUserExtensionShop() {
        return userExtensionShop;
    }

    public void setUserExtensionShop(UserEntity userExtensionShop) {
        this.userExtensionShop = userExtensionShop;
    }
}
