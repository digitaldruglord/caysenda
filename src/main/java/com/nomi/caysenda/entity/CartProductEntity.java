package com.nomi.caysenda.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cartProduct")
public class CartProductEntity extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Boolean isRetailt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId",referencedColumnName = "id")
    CartEntity cartProduct;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    List<CartVariantEntity> variants;

    @ManyToOne()
    @JoinColumn(name = "productId",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.FALSE)
    ProductEntity productCartEntity;


    public Boolean getRetailt() {
        return isRetailt;
    }

    public void setRetailt(Boolean retailt) {
        isRetailt = retailt;
    }

    public ProductEntity getProductCartEntity() {
        return productCartEntity;
    }

    public void setProductCartEntity(ProductEntity productCartEntity) {
        this.productCartEntity = productCartEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEntity getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(CartEntity cartProduct) {
        this.cartProduct = cartProduct;
    }

    public List<CartVariantEntity> getVariants() {
        return variants;
    }

    public void setVariants(List<CartVariantEntity> variants) {
        this.variants = variants;
    }
}
