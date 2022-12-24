package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "provider")
@FilterDefs({
        @FilterDef(name = "FILTER_DOMAIN", defaultCondition = "host=:domain", parameters = { @ParamDef(name = "domain", type = "string") })
})
public class ProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String providerName;
    String host;

    @ManyToMany(mappedBy = "providers")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<ProductEntity> providers;

    @ManyToMany(mappedBy = "providerOption")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<OptionEntity> optionEntities;

    @ManyToMany(mappedBy = "providerOrder")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<OrderEntity> orderEntities;

    @ManyToMany(mappedBy = "providerPage")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<PageEntity> pageEntities;

    @ManyToMany(mappedBy = "categoryProvider")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<CategoryEntity> categoryEntities;

    @ManyToMany(mappedBy = "cartProvider")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<CartEntity> cartEntities;

    @ManyToMany(mappedBy = "providerKeyword")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<KeywordEntity> keywordEntities;

    public List<KeywordEntity> getKeywordEntities() {
        return keywordEntities;
    }

    public void setKeywordEntities(List<KeywordEntity> keywordEntities) {
        this.keywordEntities = keywordEntities;
    }

    public List<CartEntity> getCartEntities() {
        return cartEntities;
    }

    public void setCartEntities(List<CartEntity> cartEntities) {
        this.cartEntities = cartEntities;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<PageEntity> getPageEntities() {
        return pageEntities;
    }

    public void setPageEntities(List<PageEntity> pageEntities) {
        this.pageEntities = pageEntities;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public List<OptionEntity> getOptionEntities() {
        return optionEntities;
    }

    public void setOptionEntities(List<OptionEntity> optionEntities) {
        this.optionEntities = optionEntities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<ProductEntity> getProviders() {
        return providers;
    }

    public void setProviders(List<ProductEntity> providers) {
        this.providers = providers;
    }
}
