package com.nomi.caysenda.entity.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "currencyDelivery")
@Getter
@Setter
public class DeliveryCurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String fullName;
    String phoneNumber;
    String status;
    Long amount;
    Date createDate;

    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    UserDeliveryEntity userDeliveryEntity;
}
