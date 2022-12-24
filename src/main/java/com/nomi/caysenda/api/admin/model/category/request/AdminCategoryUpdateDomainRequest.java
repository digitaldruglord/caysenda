package com.nomi.caysenda.api.admin.model.category.request;

import java.util.List;

public class AdminCategoryUpdateDomainRequest {
    Integer categoryId;
    List<Integer> domains;

    public AdminCategoryUpdateDomainRequest() {
    }

    public AdminCategoryUpdateDomainRequest(Integer categoryId, List<Integer> domains) {
        this.categoryId = categoryId;
        this.domains = domains;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getDomains() {
        return domains;
    }

    public void setDomains(List<Integer> domains) {
        this.domains = domains;
    }
}
