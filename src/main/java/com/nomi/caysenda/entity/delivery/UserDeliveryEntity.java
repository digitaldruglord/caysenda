package com.nomi.caysenda.entity.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nomi.caysenda.entity.ContactFormDeliveryEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "userdelivery")
@Getter
@Setter
public class UserDeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    String userName;
    String password;
    @Column(unique = true)
    String phoneNumber;
    @OneToMany(mappedBy = "userDeliveryEntity",cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<ContactFormDeliveryEntity> forms;
    @OneToMany(mappedBy = "userDeliveryEntity")
    @JsonIgnore
    List<DeliveryOrderEntity> deliveryOrderEntities;
    @OneToMany(mappedBy = "userDeliveryEntity")
    @JsonIgnore
    List<DeliveryEntity> deliveryEntities;
    @OneToMany(mappedBy = "userDeliveryEntity")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<DeliveryCurrencyEntity> currencyDeliveryEntities;


}
