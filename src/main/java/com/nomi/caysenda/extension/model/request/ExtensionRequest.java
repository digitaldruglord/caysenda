package com.nomi.caysenda.extension.model.request;

import com.nomi.caysenda.extension.model.ExtensionAttribute;
import com.nomi.caysenda.extension.model.ExtensionProduct;
import com.nomi.caysenda.extension.model.ExtensionVariant;

import java.util.List;

public class ExtensionRequest {
    String shopLink;
    ExtensionProduct product;
    List<ExtensionVariant> variants;
    List<ExtensionAttribute> attributes;
    String typeRunning;

    public String getTypeRunning() {
        return typeRunning;
    }

    public void setTypeRunning(String typeRunning) {
        this.typeRunning = typeRunning;
    }

    public List<ExtensionAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ExtensionAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getShopLink() {
        return shopLink;
    }

    public void setShopLink(String shopLink) {
        this.shopLink = shopLink;
    }

    public ExtensionProduct getProduct() {
        return product;
    }

    public void setProduct(ExtensionProduct product) {
        this.product = product;
    }

    public List<ExtensionVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ExtensionVariant> variants) {
        this.variants = variants;
    }
}
