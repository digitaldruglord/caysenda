package com.nomi.caysenda.api1688.model;

import java.util.List;

public class VariantSkuProps {
    String prop;
    List<VariantSkuPropValue> value;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public List<VariantSkuPropValue> getValue() {
        return value;
    }

    public void setValue(List<VariantSkuPropValue> value) {
        this.value = value;
    }
}
