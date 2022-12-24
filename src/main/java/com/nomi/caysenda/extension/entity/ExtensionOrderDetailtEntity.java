package com.nomi.caysenda.extension.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class ExtensionOrderDetailtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String productName;
    String variantName;
    String productThumbnail;
    String variantThumbnail;
    Integer productId;
    Integer variantId;
    String linkZh;
    String nameZh;
    String variantNameZh;
    String groupName;
    String groupZhName;
    String groupSku;
    Integer quantity;
    Integer orderedQuantity;
    String status;
    String sku;
    String variantSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    @JsonIgnore
    ExtensionOrderEntity orderEntity;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public ExtensionOrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(ExtensionOrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getVariantThumbnail() {
        return variantThumbnail;
    }

    public void setVariantThumbnail(String variantThumbnail) {
        this.variantThumbnail = variantThumbnail;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    public String getLinkZh() {
        return linkZh;
    }

    public void setLinkZh(String linkZh) {
        this.linkZh = linkZh;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getVariantNameZh() {
        return variantNameZh;
    }

    public void setVariantNameZh(String variantNameZh) {
        this.variantNameZh = variantNameZh;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupZhName() {
        return groupZhName;
    }

    public void setGroupZhName(String groupZhName) {
        this.groupZhName = groupZhName;
    }

    public String getGroupSku() {
        return groupSku;
    }

    public void setGroupSku(String groupSku) {
        this.groupSku = groupSku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(Integer orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
