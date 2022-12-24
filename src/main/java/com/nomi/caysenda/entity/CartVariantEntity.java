package com.nomi.caysenda.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table(name = "cartVariant")
public class CartVariantEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Integer quantity;
    @Column(nullable = false)
    Boolean active;

    @ManyToOne()
    @JoinColumn(name = "cartproductid",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.TRUE)
    CartProductEntity product;

    @ManyToOne()
    @JoinColumn(name = "variantId",referencedColumnName = "id")
    VariantEntity cartVariantEntity;

    String groupName;
    String groupZhName;
    String groupSku;

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public VariantEntity getCartVariantEntity() {
        return cartVariantEntity;
    }

    public void setCartVariantEntity(VariantEntity cartVariantEntity) {
        this.cartVariantEntity = cartVariantEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartProductEntity getProduct() {
        return product;
    }

    public void setProduct(CartProductEntity product) {
        this.product = product;
    }
}
