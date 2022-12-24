package com.nomi.caysenda.extension.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.extension.entity.convert.ConvertGallery;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "extensionProducts")
public class ExtensionProductEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String nameZh;
    Double price1;
    Double price2;
    Double price3;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    String link;
    @Column(columnDefinition = "text")
    String tempName;
    String standardName;
    @Column(columnDefinition = "text")
    String description;
    @Column(unique = true)
    String sku;
    Integer quantity;
    String unit;
    String unitZh;
    String video;
    @Convert(converter = ConvertGallery.class)
    @Column(columnDefinition = "text")
    List<String> gallery;
    Boolean synchronize;
    @Column(columnDefinition = "text")
    String html;
    @Convert(converter = ConvertGallery.class)
    @Column(columnDefinition = "text")
    List<String> images;
    @ManyToOne()
    @JoinColumn(name = "shopId",referencedColumnName = "id")
    @JsonIgnore
    ExtensionShopEntity shop;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnore
    List<ExtensionVariantEntity> variants;

    @OneToMany(mappedBy = "productAttribute",cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<ExtensionAttributeEntity> attributes;
    @Column(columnDefinition = "text")
    String keyword;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Boolean getSynchronize() {
        return synchronize;
    }

    public void setSynchronize(Boolean synchronize) {
        this.synchronize = synchronize;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getUnitZh() {
        return unitZh;
    }

    public void setUnitZh(String unitZh) {
        this.unitZh = unitZh;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExtensionAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ExtensionAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public List<ExtensionVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(List<ExtensionVariantEntity> variants) {
        this.variants = variants;
    }

    public ExtensionShopEntity getShop() {
        return shop;
    }

    public void setShop(ExtensionShopEntity shop) {
        this.shop = shop;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public Double getPrice1() {
        return price1;
    }

    public void setPrice1(Double price1) {
        this.price1 = price1;
    }

    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public Double getPrice3() {
        return price3;
    }

    public void setPrice3(Double price3) {
        this.price3 = price3;
    }

    public Integer getCondition1() {
        return condition1;
    }

    public void setCondition1(Integer condition1) {
        this.condition1 = condition1;
    }

    public Integer getCondition2() {
        return condition2;
    }

    public void setCondition2(Integer condition2) {
        this.condition2 = condition2;
    }

    public Integer getCondition3() {
        return condition3;
    }

    public void setCondition3(Integer condition3) {
        this.condition3 = condition3;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}
