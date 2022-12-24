package com.nomi.caysenda.extension.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nomi.caysenda.entity.UserEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "extensionShops")
public class ExtensionShopEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    String ship;
    @Column(columnDefinition = "text")
    String keyword;
    @Column(columnDefinition = "text")
    String descript;
    @Column(columnDefinition = "text")
    String html;
    @Column(columnDefinition = "boolean default true")
    Boolean enableUpdatePrice;

    @OneToMany(mappedBy = "shop",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<ExtensionProductEntity> products;

    @ManyToOne()
    @JoinTable(
            name = "user_extension_shop",
            joinColumns = @JoinColumn(name = "extensionshopId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userId",referencedColumnName = "id")
    )
    @LazyCollection(LazyCollectionOption.TRUE)
    UserEntity userExtensionShop;

    public Boolean getEnableUpdatePrice() {
        return enableUpdatePrice;
    }

    public void setEnableUpdatePrice(Boolean enableUpdatePrice) {
        this.enableUpdatePrice = enableUpdatePrice;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public UserEntity getUserExtensionShop() {
        return userExtensionShop;
    }

    public void setUserExtensionShop(UserEntity userExtensionShop) {
        this.userExtensionShop = userExtensionShop;
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

    public List<ExtensionProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ExtensionProductEntity> products) {
        this.products = products;
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
}
