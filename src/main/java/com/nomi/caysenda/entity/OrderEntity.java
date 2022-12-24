package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String billingFullName;
    private String billingPhoneNumber;
    private String billingEmail;
    private String orderComment;
    private String billingAddress;
    private String billingCity;
    private String billingDistrict;
    private String billingWards;
    @Column(columnDefinition = "boolean default false")
    private Boolean trash;
    @Column(columnDefinition = "bigint default 0")
    private Long ship;
    @Column(columnDefinition = "bigint default 0")
    private Long productAmount;
    @Column(columnDefinition = "bigint default 0")
    private Long orderAmount;
    private String status;
    private String cashflowstatus;
    @Column(columnDefinition = "bigint default 0")
    private Long refunded;
    @Column(columnDefinition = "bigint default 0")
    private Long paid;
    private String method;
    @Column(columnDefinition = "bigint default 0")
    private Long incurredCost;
    String note;
    @Column(columnDefinition="text")
    String adminNote;
    String discountType;
    Double discountValue;
    Long cod;
    String orderCode;
    @Column(columnDefinition = "bigint default 0",nullable = false)
    Long cost;
    @OneToMany(mappedBy = "orderEntity",cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<OrderDetailtEntity> detailts;

    @ManyToOne()
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    @JoinColumn(name = "userId",referencedColumnName = "id")
    UserEntity userOrder;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name = "provider_order",
            joinColumns = @JoinColumn(name = "orderId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    List<ProviderEntity> providerOrder;

    @OneToMany(mappedBy = "orderTracking",cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<TrackingOrderEntity> tracking;


}
