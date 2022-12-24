package com.nomi.caysenda.extension.model;

import java.util.List;

public class ExtensionOrderProduct {
    Integer id;
    String name;
    String nameZh;
    String link;
    List<ExtensionOrderVariant> variants;
    List<String> groups;
    Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<ExtensionOrderVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ExtensionOrderVariant> variants) {
        this.variants = variants;
    }
}
