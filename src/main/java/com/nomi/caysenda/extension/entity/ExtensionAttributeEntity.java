package com.nomi.caysenda.extension.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "extentionAttributes")
public class ExtensionAttributeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String tempName;
    String standardName;
    @Column(columnDefinition = "text")
    String value;
    @Column(columnDefinition = "text")
    String tempValue;
    String standardValue;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "productId",referencedColumnName = "id")
    ExtensionProductEntity productAttribute;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTempValue() {
        return tempValue;
    }

    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public ExtensionProductEntity getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(ExtensionProductEntity productAttribute) {
        this.productAttribute = productAttribute;
    }
}
