package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sku"}),
        @UniqueConstraint(columnNames = {"slug"})
})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(columnDefinition = "bigint factorDefault 0")
    Long conditionPurchse;
    String typeDefault;
    String type1;
    String type2;
    String type3;
    @Column(columnDefinition="text")
    String description;

    @ManyToMany(mappedBy = "categories",cascade = {CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<ProductEntity> products;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "provider_category",
            joinColumns = @JoinColumn(name = "productId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @Filters({@Filter(name = "FILTER_DOMAIN")})
    List<ProviderEntity> categoryProvider;

    public List<ProviderEntity> getCategoryProvider() {
        return categoryProvider;
    }

    public void setCategoryProvider(List<ProviderEntity> categoryProvider) {
        this.categoryProvider = categoryProvider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeDefault() {
        return typeDefault;
    }

    public void setTypeDefault(String typeDefault) {
        this.typeDefault = typeDefault;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
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

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
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
}
