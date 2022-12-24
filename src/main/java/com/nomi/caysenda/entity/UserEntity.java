package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nomi.caysenda.extension.entity.ExtensionProductEntity;
import com.nomi.caysenda.extension.entity.ExtensionShopEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"}),
                @UniqueConstraint(columnNames = {"email"})
        }
)
public class UserEntity extends BaseTimeUserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    String username;
    @JsonIgnore
    String password;
    @Column(unique = true)
    String email;
    @Column(unique = true)
    String phonenumber;
    Integer status;
    String fullName;
    @OneToMany(mappedBy = "userAddress")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<AddressEntity> address;
    @ManyToMany()
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId",referencedColumnName = "id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    List<RoleEntity> roles;

    @OneToMany(mappedBy = "userExtensionShop")
    @JsonIgnore
    List<ExtensionShopEntity> userExtensionShops;

    @OneToMany(mappedBy = "userCart")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<CartEntity> cartEntities;

    @OneToMany(mappedBy = "userOrder")
    @JsonIgnore
    List<OrderEntity> orders;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<CartEntity> getCartEntities() {
        return cartEntities;
    }

    public void setCartEntities(List<CartEntity> cartEntities) {
        this.cartEntities = cartEntities;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }


    public List<ExtensionShopEntity> getUserExtensionShops() {
        return userExtensionShops;
    }

    public void setUserExtensionShops(List<ExtensionShopEntity> userExtensionShops) {
        this.userExtensionShops = userExtensionShops;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(List<AddressEntity> address) {
        this.address = address;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }
}
