package com.nomi.caysenda.entity;

import javax.persistence.*;

@Entity
@Table(name = "attributes")
public class ProductAttributeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String attribute;
    String attributeName;
    String attributeValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
