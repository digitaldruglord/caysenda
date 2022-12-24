package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class CartEntity extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",referencedColumnName = "id")
    UserEntity userCart;
    @OneToMany(mappedBy = "cartProduct",cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<CartProductEntity> products;
    Long deliveryFee;
    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "provider_cart",
            joinColumns = @JoinColumn(name = "cartId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    @Filters({@Filter(name = "FILTER_DOMAIN")})
    List<ProviderEntity> cartProvider;
    String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ProviderEntity> getCartProvider() {
        return cartProvider;
    }

    public void setCartProvider(List<ProviderEntity> cartProvider) {
        this.cartProvider = cartProvider;
    }

    public Long getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Long deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getUserCart() {
        return userCart;
    }

    public void setUserCart(UserEntity userCart) {
        this.userCart = userCart;
    }

    public List<CartProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductEntity> products) {
        this.products = products;
    }
}
