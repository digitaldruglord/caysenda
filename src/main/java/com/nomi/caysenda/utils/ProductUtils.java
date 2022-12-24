package com.nomi.caysenda.utils;

import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.dto.cart.CartProductDTO;
import com.nomi.caysenda.dto.cart.CartVariantDTO;
import com.nomi.caysenda.entity.GroupVariantEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.utils.model.ProductAmount;
import com.nomi.caysenda.utils.model.ProductPrice;
import com.nomi.caysenda.utils.model.ProductRangeAndRetail;

public class ProductUtils {
    public static boolean isRetailt(ProductEntity productEntity) {
        return true;
    }

    public static boolean isRetailt(ProductEntity productEntity, PriceOption priceOption) {
        if (priceOption.getEnableRetail()) {
            productEntity.setConditiondefault(1);
            return true;
        }
        if (productEntity.getCondition1() != null &&
                productEntity.getCondition2() != null && productEntity.getCondition2() != 0 &&
                productEntity.getCondition3() != null && productEntity.getCondition3() != 0 &&
                productEntity.getCondition4() != null && productEntity.getCondition4() != 0) {
            if (productEntity.getVariants() != null) {
                if (productEntity.getVariants().size() > 1) {
                    for (VariantEntity variantEntity : productEntity.getVariants()) {
                        for (VariantEntity variantEntity1 : productEntity.getVariants()) {
                            Long totalPrice1 = variantEntity.getVip1() + variantEntity.getVip2() + variantEntity.getVip3() + variantEntity.getVip4();
                            Long totalPrice2 = variantEntity1.getVip1() + variantEntity1.getVip2() + variantEntity1.getVip3() + variantEntity1.getVip4();
                            if (!totalPrice1.equals(totalPrice2)) {
                                return true;
                            }
                            if (!variantEntity.getVip1().equals(variantEntity1.getVip1())) {
                                return true;
                            }
                        }
                    }
                    return false;
                } else {
                    return false;
                }

            }
        }
        return true;
    }
    public static boolean isRetailt(ProductDTO productDTO, PriceOption priceOption) {
        if (priceOption.getEnableRetail()) {
            productDTO.setConditionDefault(1);
            return true;
        }
        if (productDTO.getCondition1() != null &&
                productDTO.getCondition2() != null && productDTO.getCondition2() != 0 &&
                productDTO.getCondition3() != null && productDTO.getCondition3() != 0 &&
                productDTO.getCondition4() != null && productDTO.getCondition4() != 0) {
            return false;
        }
        return true;
    }
    public static ProductRangeAndRetail isRetailt(CartProductDTO productEntity, PriceOption priceOption) {
        ProductRangeAndRetail productRangeAndRetail = new ProductRangeAndRetail(productEntity.getRetail());
        Integer totalQuantity = 0;
        Integer totalQuantityActive = 0;
        Long amount = Long.valueOf(0);
        Long amountActive = Long.valueOf(0);
        Boolean active = true;
        if (!productEntity.getRetail()) {
            for (CartVariantDTO variantEntity : productEntity.getVariants()) {
                totalQuantity += variantEntity.getQuantity();
                if (variantEntity.getActive()) {
                    totalQuantityActive += variantEntity.getQuantity();
                }
                if (!variantEntity.getActive()) active = false;
            }
        } else {
            for (CartVariantDTO cartVariantDTO : productEntity.getVariants()) {
                if (cartVariantDTO.getActive()) {
                    amountActive += getTypePrice(priceOption.getPriceDefault(), cartVariantDTO.getVip1(), cartVariantDTO.getVip2(), cartVariantDTO.getVip3(), cartVariantDTO.getVip4(), cartVariantDTO.getPriceDefault()) * cartVariantDTO.getQuantity();
                    totalQuantityActive += cartVariantDTO.getQuantity();
                }
                if (!cartVariantDTO.getActive()) active = false;
                amount += getTypePrice(priceOption.getPriceDefault(), cartVariantDTO.getVip1(), cartVariantDTO.getVip2(), cartVariantDTO.getVip3(), cartVariantDTO.getVip4(), cartVariantDTO.getPriceDefault()) * cartVariantDTO.getQuantity();
                totalQuantity += cartVariantDTO.getQuantity();
            }
        }

        productRangeAndRetail.setActive(active);
        productRangeAndRetail.setTotalQuantity(totalQuantity);
        productRangeAndRetail.setAmount(amount);
        productRangeAndRetail.setAmountActive(amountActive);
        productRangeAndRetail.setTotalQuantityActive(totalQuantityActive);
        if (!productRangeAndRetail.getRetail()) {
            productRangeAndRetail.setRange(getRange(totalQuantity, productEntity.getConditionDefault(), productEntity.getCondition1(), productEntity.getCondition2(), productEntity.getCondition3(), productEntity.getCondition4()));
            productRangeAndRetail.setRangeActive(getRange(totalQuantityActive, productEntity.getConditionDefault(), productEntity.getCondition1(), productEntity.getCondition2(), productEntity.getCondition3(), productEntity.getCondition4()));
        }
        return productRangeAndRetail;
    }

    public static Integer getRange(Integer quantity, Integer conditionDefault, Integer condition1, Integer condition2, Integer condition3, Integer condition4) {
        if (condition1 == null || condition2 == null || condition3 == null || condition4 == null || conditionDefault == null)
            return 0;
        if (quantity >= conditionDefault && quantity < condition1) {
            return 1;
        } else if (quantity >= condition1 && quantity < condition2) {
            return 2;
        } else if (quantity >= condition2 && quantity < condition3) {
            return 3;
        } else if (quantity >= condition3) {
            return 3;
        }
        return 0;
    }

    public static ProductAmount getPrice(Integer range, Integer quantity, Long price1, Long price2, Long price3) {
        switch (range) {
            case 1:
                return new ProductAmount(price1 * quantity, price1, quantity);
            case 2:
                return new ProductAmount(price2 * quantity, price2, quantity);
            case 3:
                return new ProductAmount(price3 * quantity, price3, quantity);
            default:
                return new ProductAmount(Long.valueOf(0), Long.valueOf(0), quantity);
        }
    }

    public static Long getTypePrice(CartProductDTO productDTO, String type) {
        switch (type) {
            case "default":
                return productDTO.getPriceDefault();
            case "vip1":
                return productDTO.getPrice1();
            case "vip2":
                return productDTO.getPrice2();
            case "vip3":
                return productDTO.getPrice3();
            case "vip4":
                return productDTO.getPrice4();
        }
        return null;
    }

    public static Long getTypePrice(String type, Long price1, Long price2, Long price3, Long price4, Long priceDefault) {
        switch (type) {
            case "default":
                return priceDefault;
            case "vip1":
                return price1;
            case "vip2":
                return price2;
            case "vip3":
                return price3;
            case "vip4":
                return price4;
        }
        return Long.valueOf(0);
    }

    public static void getFullDataCartProductDTO(CartProductDTO productDTO, ProductRangeAndRetail rangeAndRetail, PriceOption priceOption) {
        productDTO.setQuantity(rangeAndRetail.getTotalQuantity());
        productDTO.setRetail(rangeAndRetail.getRetail());
        productDTO.setRange(rangeAndRetail.getRangeActive());
        productDTO.setActive(rangeAndRetail.getActive());
        productDTO.setQuantityActive(rangeAndRetail.getTotalQuantityActive());
        if (!rangeAndRetail.getRetail()) {
            productDTO.setPrice1(productDTO.getVariants().get(0).getVip1());
            productDTO.setPrice2(productDTO.getVariants().get(0).getVip2());
            productDTO.setPrice3(productDTO.getVariants().get(0).getVip3());
            productDTO.setPriceDefault(productDTO.getVariants().get(0).getPrice());
            productDTO.setPrice4(productDTO.getVariants().get(0).getVip4());
            ProductAmount productAmount = ProductUtils.getPrice(
                    rangeAndRetail.getRange(),
                    rangeAndRetail.getTotalQuantity(),
                    getTypePrice(productDTO, priceOption.getPrice1()),
                    getTypePrice(productDTO, priceOption.getPrice2()),
                    getTypePrice(productDTO, priceOption.getPrice3()));
            ProductAmount productAmountActive = ProductUtils.getPrice(
                    rangeAndRetail.getRangeActive(),
                    rangeAndRetail.getTotalQuantityActive(),
                    getTypePrice(productDTO, priceOption.getPrice1()),
                    getTypePrice(productDTO, priceOption.getPrice2()),
                    getTypePrice(productDTO, priceOption.getPrice3()));
            productDTO.setAmount(productAmount.getAmount());
            productDTO.setAmountActive(productAmountActive.getAmount());
        } else {
            productDTO.setAmount(rangeAndRetail.getAmount());
            productDTO.setAmountActive(rangeAndRetail.getAmountActive());
        }
    }

    public static GroupVariantEntity getGroupByGroupSku(ProductEntity productEntity, String sku) {
        if (sku==null) return null;
        if (productEntity.getVariantGroup() != null) {
            for (GroupVariantEntity groupVariantEntity:productEntity.getVariantGroup()){
                if (groupVariantEntity.getSkuGroup().equals(sku)){
                    return groupVariantEntity;
                }
            }
        }
        return null;
    }
}
