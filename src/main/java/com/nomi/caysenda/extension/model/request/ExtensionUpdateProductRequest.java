package com.nomi.caysenda.extension.model.request;

import java.util.List;

public class ExtensionUpdateProductRequest {
    Integer id;
    String description;
    String standardName;
    String unit;
    List<ExtensionUpdateVariantRequest> variants;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ExtensionUpdateVariantRequest> getVariants() {
        return variants;
    }

    public void setVariants(List<ExtensionUpdateVariantRequest> variants) {
        this.variants = variants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }
}
