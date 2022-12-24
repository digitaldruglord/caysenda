package com.nomi.caysenda.dialect.utils;

import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.utils.ProductUtils;

import java.text.DecimalFormat;
import java.util.NoSuchElementException;

public class ProductUtilsDialect {
    public static DecimalFormat format = new DecimalFormat("###,###,###");
    public ProductUtilsDialect() {
    }
    public static String getSaleTag(PriceOption priceOption,ProductDTO productDTO){
        if (priceOption.getEnableRetail()){
            return "<span class=\"sale\">-50%</span>";
        }
        return "";
    }
    public String getPriceByType(String type,Long price1,Long price2,Long price3,Long price4,Long priceDefault){
        return  format.format(ProductUtils.getTypePrice(type, price1, price2, price3, price4, priceDefault))+"đ";
    }
    public Long getPriceByTypeNF(String type,Long price1,Long price2,Long price3,Long price4,Long priceDefault){
        return ProductUtils.getTypePrice(type, price1, price2, price3, price4, priceDefault);
    }
    public String getPriceByType(String type, ProductDTO productDTO){
        return  format.format(ProductUtils.getTypePrice(type, productDTO.getMinVip1(), productDTO.getMinVip2(), productDTO.getMinVip3(), productDTO.getMinVip4(), productDTO.getMinPrice()))+"đ";
    }
    public String getPriceByType(PriceOption priceOption, ProductDTO productDTO){
        if (priceOption.getEnableRetail()){
            return "<del style='color: gray;'>"+format.format(ProductUtils.getTypePrice(priceOption.getPriceDefault(), productDTO.getMinVip1()*2, productDTO.getMinVip2()*2, productDTO.getMinVip3()*2, productDTO.getMinVip4()*2, productDTO.getMinPrice()*2))+"đ"+"</del> "+ format.format(ProductUtils.getTypePrice(priceOption.getPriceDefault(), productDTO.getMinVip1(), productDTO.getMinVip2(), productDTO.getMinVip3(), productDTO.getMinVip4(), productDTO.getMinPrice()))+"đ";
        }
        if (productDTO==null) return "0";
        if (productDTO.getMinPrice().equals(productDTO.getMaxPrice())){
            if (productDTO.getConditionDefault()!=null &&
                    productDTO.getCondition1()!=null && productDTO.getCondition1()!=0 &&
                    productDTO.getCondition2()!=null && productDTO.getCondition2()!=0 &&
                    productDTO.getCondition3()!=null && productDTO.getCondition3()!=0 &&
                    productDTO.getCondition4()!=null && productDTO.getCondition4()!=0 ){
                return  format.format(ProductUtils.getTypePrice(priceOption.getPrice3(), productDTO.getMinVip1(), productDTO.getMinVip2(), productDTO.getMinVip3(), productDTO.getMinVip4(), productDTO.getMinPrice()))+"đ";
            }else {
                return  format.format(ProductUtils.getTypePrice(priceOption.getPriceDefault(), productDTO.getMinVip1(), productDTO.getMinVip2(), productDTO.getMinVip3(), productDTO.getMinVip4(), productDTO.getMinPrice()))+"đ";
            }
        }else {
            return  format.format(ProductUtils.getTypePrice(priceOption.getPriceDefault(), productDTO.getMinVip1(), productDTO.getMinVip2(), productDTO.getMinVip3(), productDTO.getMinVip4(), productDTO.getMinPrice()))+"đ";
        }
    }
    public String getPriceByType(String typePrice, ProductEntity productEntity){
        if (productEntity.getVariants()==null) return "0";
        Long priceDefailt = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip1();
        Long vip1 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip1();
        Long vip2 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip2();
        Long vip3 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip3();
        Long vip4 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip4();
        return  format.format(ProductUtils.getTypePrice(typePrice, vip1, vip2, vip3, vip4, priceDefailt))+"đ";
    }
    public String getRegularPriceByType(String typePrice, ProductEntity productEntity,String order){
        if (order.equals("min")){
            return  format.format(getMinPrice(productEntity,typePrice)*2)+"đ";

        }else {
            return format.format(getMaxPrice(productEntity,typePrice)*2)+"đ";
        }
    }
    public String getPriceByType(String typePrice, ProductEntity productEntity,String order){
       if (order.equals("min")){
           return  format.format(getMinPrice(productEntity,typePrice))+"đ";

       }else {
           return format.format(getMaxPrice(productEntity,typePrice))+"đ";
       }
    }
    public Long getPriceByTypeNF(String typePrice, ProductEntity productEntity){
        if (productEntity.getVariants()==null) return Long.valueOf(0);
        Long priceDefailt = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip1();
        Long vip1 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip1();
        Long vip2 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip2();
        Long vip3 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip3();
        Long vip4 = productEntity.getVariants().size()<=0?0: productEntity.getVariants().get(0).getVip4();
        return  ProductUtils.getTypePrice(typePrice, vip1, vip2, vip3, vip4, priceDefailt);
    }
    public Long getPriceByTypeNF(String typePrice, ProductEntity productEntity,String order){
        if (order.equals("min")){
            return  getMinPrice(productEntity,typePrice);

        }else {
            return getMaxPrice(productEntity,typePrice);
        }
    }
    private Long getMinPrice(ProductEntity productEntity,String type){
        switch (type){
            case "default": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getPrice())
                    .min().orElseThrow(NoSuchElementException::new);
            case "vip1": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip1())
                    .min().orElseThrow(NoSuchElementException::new);
            case "vip2": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip2())
                    .min().orElseThrow(NoSuchElementException::new);
            case "vip3": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip3())
                    .min().orElseThrow(NoSuchElementException::new);
            case "vip4": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip4())
                    .min().orElseThrow(NoSuchElementException::new);
        }
       return Long.valueOf(0);
    }
    private Long getMaxPrice(ProductEntity productEntity,String type){
        switch (type){
            case "default":
                return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getPrice())
                    .max().orElseThrow(NoSuchElementException::new);
            case "vip1": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip1())
                    .max().orElseThrow(NoSuchElementException::new);
            case "vip2": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip2())
                    .max().orElseThrow(NoSuchElementException::new);
            case "vip3": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip3())
                    .max().orElseThrow(NoSuchElementException::new);
            case "vip4": return productEntity.getVariants().size()<=0?0:productEntity.getVariants()
                    .stream()
                    .mapToLong(v -> v.getVip4())
                    .max().orElseThrow(NoSuchElementException::new);
        }
        return Long.valueOf(0);
    }
}
