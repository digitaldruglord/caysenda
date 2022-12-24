package com.nomi.caysenda.entity.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "deliveryOrder")
@Getter
@Setter
public class DeliveryOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String fullName;
    String phoneNumber;
    String productName;
    Integer quantity;
    String address;
    @Column(columnDefinition = "text")
    String link;
    Long amount;
    Long cost;
    Long fee;
    Long paid;
    String zhId;
    String viId;
    Date createDate;
    @ManyToOne()
    @JoinColumn(name = "userdeliveryid",referencedColumnName = "id")
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    UserDeliveryEntity userDeliveryEntity;

    @OneToMany(mappedBy = "deliveryOrderEntity",cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<DeliveryOrderStatus> statusList;

}
