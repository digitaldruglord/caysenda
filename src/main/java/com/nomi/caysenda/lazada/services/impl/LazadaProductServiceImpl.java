package com.nomi.caysenda.lazada.services.impl;

import com.google.gson.Gson;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.lazada.api.LazopClient;
import com.nomi.caysenda.lazada.api.LazopRequest;
import com.nomi.caysenda.lazada.api.LazopResponse;
import com.nomi.caysenda.lazada.constants.LazadaContant;
import com.nomi.caysenda.lazada.entity.LazadaProductEntity;
import com.nomi.caysenda.lazada.entity.LazadaVariantEntity;
import com.nomi.caysenda.lazada.repositories.LazadaProductRepository;
import com.nomi.caysenda.lazada.repositories.LazadaVariantRepository;
import com.nomi.caysenda.lazada.services.LazadaProductService;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.lazada.services.model.*;
import com.nomi.caysenda.lazada.services.xml.LazadaAttributesXml;
import com.nomi.caysenda.lazada.services.xml.LazadaProductXml;
import com.nomi.caysenda.lazada.services.xml.LazadaRequestXml;
import com.nomi.caysenda.lazada.services.xml.LazadaSkuXml;
import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.redis.model.RedisProgress;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.services.ProgressService;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class LazadaProductServiceImpl implements LazadaProductService {
    @Autowired
    ProductService productService;
    @Autowired
    LazadaProductRepository lazadaProductRepository;
    @Autowired
    LazadaVariantRepository lazadaVariantRepository;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    ProgressService progressService;
    @Autowired
    LazadaSystemService lazadaSystemService;

    @Override
    public LazadaProductEntity createByProductId(Integer productId, Integer categoryId, Integer lazadaCategoryId) throws Exception {
        ProductEntity productEntity = productService.findById(productId);
        LazadaCategoryAttributeResponse categoryAttributeResponse = getCategoryAttributes(lazadaCategoryId);
        LazadaRequestXml lazadaRequestXml = new LazadaRequestXml();
        List<LazadaProductXml> productsxml = lazadaRequestXml.getProducts()!=null?lazadaRequestXml.getProducts():new ArrayList<>();
        Boolean isSaleProp = isSaleProp(categoryAttributeResponse.getData());
        if (productEntity.getVariantGroup()!=null && isSaleProp && productEntity.getVariantGroup().size()>0){
            LazadaProductXml lazadaProductXml = new LazadaProductXml();
            /** product */
            lazadaProductXml.setSPUId(String.valueOf(productEntity.getId()));
            lazadaProductXml.setAssociatedSku(productEntity.getSkuVi());
            lazadaProductXml.setPrimaryCategory(lazadaCategoryId);
            lazadaProductXml.setImages(productEntity.getGallery().stream().map(s -> "https://caysenda.vn"+s).collect(Collectors.toList()).subList(0,productEntity.getGallery().size()>5?5:productEntity.getGallery().size()));
            /** attribute */
            LazadaAttributesXml lazadaAttributesXml = new LazadaAttributesXml();
            lazadaAttributesXml.setName(productEntity.getNameVi());
            lazadaAttributesXml.setShort_description(productEntity.getDescription()!=null?productEntity.getDescription():"");
            lazadaAttributesXml.setBrand("Remark");
            lazadaAttributesXml.setModel("asdf");
            lazadaAttributesXml.setKid_years("Kids (6-10yrs)");
            lazadaAttributesXml.setDelivery_option_sof("No");
            /** skus */
            List<LazadaSkuXml> variantsXml = new ArrayList<>();
            productEntity.getVariantGroup().forEach(groupVariantEntity -> {
                for (VariantEntity variantEntity:productEntity.getVariants()){
                    if (variantEntity.getParent().equals(groupVariantEntity.getSkuGroup())){
                        LazadaSkuXml lazadaSkuXml = new LazadaSkuXml();
                        lazadaSkuXml.setSellerSku(variantEntity.getSkuVi());
                        lazadaSkuXml.setColor_family(groupVariantEntity.getName());
                        lazadaSkuXml.setSize(variantEntity.getNameVi());
                        lazadaSkuXml.setQuantity(variantEntity.getStock());
                        lazadaSkuXml.setPrice(variantEntity.getPrice());
                        lazadaSkuXml.setPackage_length(variantEntity.getLength()!=null?variantEntity.getLength():10);
                        lazadaSkuXml.setPackage_height(variantEntity.getHeight()!=null?variantEntity.getHeight():10);
                        lazadaSkuXml.setPackage_width(variantEntity.getWidth()!=null?variantEntity.getWidth():10);
                        lazadaSkuXml.setPackage_weight(variantEntity.getWeight()!=null?(variantEntity.getWeight()<=300?variantEntity.getWeight():300):10);
                        lazadaSkuXml.setPackage_content(productEntity.getDescription());
                        if (groupVariantEntity.getThumbnail()!=null){
                            lazadaSkuXml.setImages(List.of("https://caysenda.vn"+groupVariantEntity.getThumbnail()));
                        }
                        if (!isVariantColorAndSizeExists(variantsXml,lazadaSkuXml.getColor_family(),lazadaSkuXml.getSize())){
                            variantsXml.add(lazadaSkuXml);
                        }
                    }
                }
            });
            lazadaProductXml.setSkus(variantsXml);
            lazadaProductXml.setAttributes(lazadaAttributesXml);
            productsxml.add(lazadaProductXml);


        }else {
            LazadaProductXml lazadaProductXml = new LazadaProductXml();
            /** product */
            lazadaProductXml.setSPUId(String.valueOf(productEntity.getId()));
            lazadaProductXml.setAssociatedSku(productEntity.getSkuVi());
            lazadaProductXml.setPrimaryCategory(7609);
            lazadaProductXml.setImages(productEntity.getGallery().stream().map(s -> "https://caysenda.vn"+s).collect(Collectors.toList()).subList(0,productEntity.getGallery().size()>5?5:productEntity.getGallery().size()));
            /** attribute */
            LazadaAttributesXml lazadaAttributesXml = new LazadaAttributesXml();
            lazadaAttributesXml.setName(productEntity.getNameVi());
            lazadaAttributesXml.setShort_description(productEntity.getDescription()!=null?productEntity.getDescription():"");
            lazadaAttributesXml.setBrand("Remark");
            lazadaAttributesXml.setModel("asdf");
            lazadaAttributesXml.setKid_years("Kids (6-10yrs)");
            lazadaAttributesXml.setDelivery_option_sof("No");
            /** skus */
            List<LazadaSkuXml> variantsXml = new ArrayList<>();
            AtomicReference<Integer> i= new AtomicReference<>(0);
            productEntity.getVariants().forEach(variantEntity -> {
                LazadaSkuXml lazadaSkuXml = new LazadaSkuXml();
                lazadaSkuXml.setSellerSku(variantEntity.getSkuVi());
                lazadaSkuXml.setColor_family(variantEntity.getNameVi());
                lazadaSkuXml.setQuantity(variantEntity.getStock());
                lazadaSkuXml.setPrice(variantEntity.getPrice());
                lazadaSkuXml.setPackage_length(variantEntity.getLength()!=null?variantEntity.getLength():10);
                lazadaSkuXml.setPackage_height(variantEntity.getHeight()!=null?variantEntity.getHeight():10);
                lazadaSkuXml.setPackage_width(variantEntity.getWidth()!=null?variantEntity.getWidth():10);
                lazadaSkuXml.setPackage_weight(variantEntity.getWeight()!=null?(variantEntity.getWeight()<=300?variantEntity.getWeight():300):10);
                lazadaSkuXml.setPackage_content(productEntity.getDescription());
                if (variantEntity.getThumbnail()!=null){
                    lazadaSkuXml.setImages(List.of("https://caysenda.vn"+variantEntity.getThumbnail()));
                }
                if (!isVariantExists(variantsXml,lazadaSkuXml.getColor_family())){
                    variantsXml.add(lazadaSkuXml);
                }

            });
            lazadaProductXml.setSkus(variantsXml);
            lazadaProductXml.setAttributes(lazadaAttributesXml);
            productsxml.add(lazadaProductXml);
        }

        lazadaRequestXml.setProducts(productsxml);
        String xml = generateXml(lazadaRequestXml);
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setHttpMethod("POST");
        request.setApiName("/product/create");
        request.addApiParameter("payload", xml);
        LazopResponse response = client.execute(request,"50000800235yauUobASPks8joRWmrEayKvgotfxuEZgBUhl177de6cdTVCBml5Eg");
        LazadaCreateProductResponse createProductResponse = new Gson().fromJson(response.getBody(), LazadaCreateProductResponse.class);
        if (createProductResponse.getCode().equals(0)){
            LazadaProductEntity lazadaProductEntity = new LazadaProductEntity();
            lazadaProductEntity.setItem_id(createProductResponse.getData().getItem_id());
            lazadaProductEntity.setProductSku(productEntity.getSkuVi());
            lazadaProductEntity.setCategoryId(categoryId);
            lazadaProductEntity = lazadaProductRepository.save(lazadaProductEntity);
            List<LazadaVariantEntity> lazadaVariantEntities = new ArrayList<>();
            for (LazadaSkuList lazadaSkuList:createProductResponse.getData().getSku_list()){
                LazadaVariantEntity lazadaVariantEntity = new LazadaVariantEntity();
                lazadaVariantEntity.setSku_id(lazadaSkuList.getSku_id());
                lazadaVariantEntity.setSeller_sku(lazadaSkuList.getSeller_sku());
                lazadaVariantEntity.setShop_sku(lazadaSkuList.getShop_sku());
                lazadaVariantEntity.setLazadaProductEntity(lazadaProductEntity);
                lazadaVariantEntity.setCategoryId(categoryId);
                lazadaVariantEntities.add(lazadaVariantEntity);
            }

            lazadaVariantEntities = lazadaVariantRepository.saveAll(lazadaVariantEntities);
            lazadaProductEntity.setVariants(lazadaVariantEntities);
            return  lazadaProductEntity;
        }
        return null;

    }

    @Override
    public void createByCategoryId(Integer categoryId, Integer lazadaCategoryId) throws Exception {
        List<ProductEntity> productEntities = productService.findAllByCat(categoryId);
        List<LazadaProductEntity> lazadaProductEntities = new ArrayList<>();
        String PROGRESS_CODE = "PROGRESS_IMAGE_ZIP";
        progressService.save(0,100,PROGRESS_CODE,true);
        taskScheduler.schedule(() -> {
            Integer i = 1;
            for (ProductEntity productEntity:productEntities){
                RedisProgress redisProgress = progressService.findByCode(PROGRESS_CODE);
                if (!redisProgress.getRunning()){
                    break;
                }
                LazadaProductEntity lazadaProductEntity = null;
                try {
                    lazadaProductEntity = createByProductId(productEntity.getId(), categoryId, lazadaCategoryId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (lazadaProductEntity!=null){
                    lazadaProductEntities.add(lazadaProductEntity);
                }
                progressService.save(i++,productEntities.size(),PROGRESS_CODE,true);
            }
            progressService.delete(PROGRESS_CODE);
        },new Date());


    }

    Boolean isSaleProp(List<LazadaCategoryAttribute> attributes){
        Boolean checkColorFamily = false;
        Boolean checkSize = false;

        for (LazadaCategoryAttribute lazadaCategoryAttribute:attributes){
            if (lazadaCategoryAttribute.getName().equals("color_family") && lazadaCategoryAttribute.getIs_sale_prop().equals(1)){
                checkColorFamily = true;
            }
            if (lazadaCategoryAttribute.getName().equals("size") && lazadaCategoryAttribute.getIs_sale_prop().equals(1)){
                checkSize = true;
            }
        }

        return checkColorFamily && checkSize;
    }
    Boolean isVariantExists(List<LazadaSkuXml> lazadaSkuXmls,String colorFamily){
        if (lazadaSkuXmls.size()<=0) return false;
        for (LazadaSkuXml lazadaSkuXml:lazadaSkuXmls){
            if (lazadaSkuXml.getColor_family().trim().equalsIgnoreCase(colorFamily)){
                return true;
            }
        }
        return false;
    }
    Boolean isVariantColorAndSizeExists(List<LazadaSkuXml> lazadaSkuXmls,String color,String size){
        if (lazadaSkuXmls.size()<=0) return false;
        for (LazadaSkuXml lazadaSkuXml:lazadaSkuXmls){
            if (lazadaSkuXml.getColor_family().trim().equalsIgnoreCase(color) && lazadaSkuXml.getSize().trim().equalsIgnoreCase(size)){
                return true;
            }
        }
        return false;
    }
    @Override
    public LazadaCategoryResponse getCategoryTree() throws ApiException {
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/category/tree/get");
        request.setHttpMethod("GET");
        request.addApiParameter("language_code", "vi_VN");
        LazopResponse response = client.execute(request);
        return new Gson().fromJson(response.getBody(), LazadaCategoryResponse.class);

    }

    @Override
    public LazadaBrandResponse getBrandByPages(Integer page, Integer pageSize) throws ApiException {
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/category/brands/query");
        request.addApiParameter("startRow", page!=null?String.valueOf(page):"0");
        request.addApiParameter("pageSize",  pageSize!=null?String.valueOf(pageSize):"20");
        request.addApiParameter("languageCode", "en_US");
        LazopResponse response = client.execute(request);
        return new Gson().fromJson(response.getBody(), LazadaBrandResponse.class);
    }

    @Override
    public LazadaCategoryAttributeResponse getCategoryAttributes(Integer categoryId) throws ApiException {
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/category/attributes/get");
        request.setHttpMethod("GET");
        request.addApiParameter("primary_category_id", String.valueOf(categoryId));
        request.addApiParameter("language_code", "vi_VN");
        LazopResponse response = client.execute(request);
        return new Gson().fromJson(response.getBody(), LazadaCategoryAttributeResponse.class);
    }

    @Override
    public LazadaCategorySuggestionResponse getCategorySuggestion() throws ApiException {
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/product/category/suggestion/get");
        request.setHttpMethod("GET");
        request.addApiParameter("product_name", "Pin dự phòng");
        request.addApiParameter("language_code", "vi_VN");
        LazopResponse response = client.execute(request, "50000800235yauUobASPks8joRWmrEayKvgotfxuEZgBUhl177de6cdTVCBml5Eg");
        return new Gson().fromJson(response.getBody(), LazadaCategorySuggestionResponse.class);
    }

    @Override
    public void updateProduct() {
        ProductEntity productEntity = productService.findById(12);


    }

    @Override
    public void removeByProductSku(String sku) throws ApiException {
        LazadaProductEntity lazadaProductEntity = lazadaProductRepository.findByProductSku(sku);
        List<LazadaVariantEntity> variantEntities = lazadaVariantRepository.findAllByProductItemId(lazadaProductEntity.getItem_id());
        for (LazadaVariantEntity lazadaVariantEntity:variantEntities){
            String[] skudata = lazadaVariantEntity.getShop_sku().split("_");
            String productId = skudata[0];
            String variantId = skudata[1].split("-")[1];
            String skuRemove = "SkuId_"+productId+"_"+variantId;
            removeBySku(skuRemove);
            lazadaVariantRepository.delete(lazadaVariantEntity);
        }
        lazadaProductRepository.delete(lazadaProductEntity);
    }

    @Override
    public void removeBySku(String id) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return ;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/product/remove");
        request.addApiParameter("seller_sku_list",new Gson().toJson(List.of(id)));
        LazopResponse response = client.execute(request, (String) token.get("token"));

    }

    @Override
    public void removeBySku(List<String> skus) throws ApiException {
        Map token = lazadaSystemService.getToken();
        if (token == null) return ;
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/product/remove");
        request.addApiParameter("seller_sku_list",new Gson().toJson(skus));
        LazopResponse response = client.execute(request, (String) token.get("token"));
    }

    @Override
    public void removeProductByCatId(Integer categoryId) throws ApiException {

        List<String> skus = new ArrayList<>();
        List<LazadaProductEntity> productEntities = lazadaProductRepository.findAllByCategoryId(categoryId);
        for (LazadaProductEntity lazadaProductEntity:productEntities){
            List<LazadaVariantEntity> variantEntities = lazadaVariantRepository.findAllByProductItemId(lazadaProductEntity.getItem_id());
            for (LazadaVariantEntity lazadaVariantEntity:variantEntities){
                String[] skudata = lazadaVariantEntity.getShop_sku().split("_");
                String productId = skudata[0];
                String variantId = skudata[1].split("-")[1];
                String skuRemove = "SkuId_"+productId+"_"+variantId;
                removeBySku(skuRemove);
                skus.add(skuRemove);
                lazadaVariantRepository.delete(lazadaVariantEntity);
            }
            lazadaProductRepository.delete(lazadaProductEntity);
        }

    }

    private String generateXml(LazadaRequestXml lazadaRequestXml) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Serializer serializer = new Persister();
        serializer.write(lazadaRequestXml,outputStream);
        return new String(outputStream.toByteArray(),"utf-8")
                .replace("string","Image");
    }
}
