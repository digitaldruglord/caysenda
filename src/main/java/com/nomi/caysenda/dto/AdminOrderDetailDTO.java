package com.nomi.caysenda.dto;

public interface AdminOrderDetailDTO {
    Integer getId();

    Integer getQuantity();

    Integer getOwe();

    Long getPrice();

    Double getPriceCN();

    Long getCost();

    String getName();

    String getVariantName();

    String getProductThumbnail();

    String getVariantThumbnail();

    Integer getProductId();

    Integer getVariantId();

    Float getWeight();

    String getSlug();

    String getLinkZh();

    String getSku();

    String getNameZh();

    String getVariantNameZh();

    String getGroupName();

    String getGroupZhName();

    String getGroupSku();

    Integer getCategoryId();

    String getUnit();

    String getTopFlag();
}
