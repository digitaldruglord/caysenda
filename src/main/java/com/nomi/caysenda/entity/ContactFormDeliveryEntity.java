package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nomi.caysenda.entity.delivery.UserDeliveryEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Table(name = "contactform")
@Getter
@Setter
public class ContactFormDeliveryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    UserDeliveryEntity userDeliveryEntity;
    @Column(columnDefinition = "text")
    String data;
    String type;

}
