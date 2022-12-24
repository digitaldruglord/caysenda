package com.nomi.caysenda.utils;

import com.nomi.caysenda.api.admin.model.category.request.AdminCategoryRequest;
import com.nomi.caysenda.entity.CategoryEntity;

public class CategoryUtils {
    public static CategoryEntity convertAdminCategoryRequestToEntity(AdminCategoryRequest categoryRequest){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryRequest.getName());
        categoryEntity.setSku(categoryRequest.getSku());
        categoryEntity.setSlug(ConvertStringToUrl.covertStringToURL(categoryRequest.getName()));
        categoryEntity.setActive(categoryRequest.getActive());
        categoryEntity.setRate(categoryRequest.getRate());
        categoryEntity.setFactorDefault(categoryRequest.getFactorDefault());
        categoryEntity.setFactor1(categoryRequest.getFactor1());
        categoryEntity.setFactor2(categoryRequest.getFactor2());
        categoryEntity.setFactor3(categoryRequest.getFactor3());
        categoryEntity.setFactor4(categoryRequest.getFactor4());
        categoryEntity.setParent(categoryRequest.getParent());
        categoryEntity.setThumbnail(categoryRequest.getThumbnail());
        categoryEntity.setBanner(categoryRequest.getBanner());
        categoryEntity.setConditionPurchse(categoryRequest.getConditionPurchse());
        categoryEntity.setDescription(categoryRequest.getDescription());
        return categoryEntity;
    }
}
