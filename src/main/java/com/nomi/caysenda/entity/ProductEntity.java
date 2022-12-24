package com.nomi.caysenda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "products",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"skuZh"}),
        @UniqueConstraint(columnNames = {"slugVI"}),
        @UniqueConstraint(columnNames = {"skuVI"}),
})
@FilterDefs({
        @FilterDef(name = "FILTER_DOMAIN_NOT_EMPTY", defaultCondition = "count(providers)>:condition", parameters = { @ParamDef(name = "condition", type = "integer") }),
})
@Getter
@Setter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String nameZh;
    String skuZh;
    String nameVi;
    String slugVi;
    String skuVi;
    String thumbnail;
    @Convert(converter = GalleryEntity.class)
    @Column(columnDefinition="text")
    List<String> gallery;
    @Convert(converter = ConvertQuickviewGallery.class)
    @Column(columnDefinition="text")
    List<String> quickviewGallery;
    Integer status;
    @Column(columnDefinition="text")
    String content;
    Boolean trash;
    Integer conditiondefault;
    Integer condition1;
    Integer condition2;
    Integer condition3;
    Integer condition4;
    Integer categoryDefault;
    Boolean enableAutoUpdatePrice;
    String link;
    String unit;
    String material;
    @Column(columnDefinition="text")
    String description;
    String averageWeight;
    Long sold;
    @OneToMany(mappedBy = "productEntity",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<VariantEntity> variants;
    @Convert(converter = GroupVariantConvert.class)
    @Column(columnDefinition="text")
    List<GroupVariantEntity> variantGroup;
    String video;
    @Column(columnDefinition="text")
    String keyword;
    @Column(columnDefinition = "varchar(1) default '0'")
    String topFlag;

    @ManyToMany()
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "productid",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "categoryid",referencedColumnName = "id")
    )
    List<CategoryEntity> categories;
    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "provider_product",
            joinColumns = @JoinColumn(name = "productId",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "providerId",referencedColumnName = "id")
    )
    @JsonIgnore
    @Filters({@Filter(name = "FILTER_DOMAIN")})
    List<ProviderEntity> providers;

    @OneToMany(mappedBy = "productCartEntity",cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JsonIgnore
    List<CartProductEntity> cartProducts;
}
