package com.nomi.caysenda.api.admin.model.category.request;

import java.util.List;

public class AdminCategoryUpdateSortRequest {
    String title;
    AdminCategoryRequest categoryRequest;
    List<AdminCategoryUpdateSortRequest> children;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdminCategoryRequest getCategoryRequest() {
        return categoryRequest;
    }

    public void setCategoryRequest(AdminCategoryRequest categoryRequest) {
        this.categoryRequest = categoryRequest;
    }

    public List<AdminCategoryUpdateSortRequest> getChildren() {
        return children;
    }

    public void setChildren(List<AdminCategoryUpdateSortRequest> children) {
        this.children = children;
    }
}
