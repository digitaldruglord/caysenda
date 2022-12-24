package com.nomi.caysenda.entity.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "deliveryStatus")
@Getter
@Setter
public class DeliveryStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String status;
    String note;
    Date createDate;
    @ManyToOne() @JsonIgnore
    @JoinColumn(name = "deliveryid",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.TRUE)
    DeliveryEntity deliveryEntity;
}
