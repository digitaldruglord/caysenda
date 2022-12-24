package com.nomi.caysenda.extension.controller.impl;

import com.nomi.caysenda.entity.TrackingOrderEntity;
import com.nomi.caysenda.exceptions.product.ProductException;
import com.nomi.caysenda.extension.controller.ExtensionControllerAPI;
import com.nomi.caysenda.extension.entity.ExtensionProductEntity;
import com.nomi.caysenda.extension.entity.ExtensionShopEntity;
import com.nomi.caysenda.extension.entity.ExtensionVariantEntity;
import com.nomi.caysenda.extension.model.ExtensionOrder;
import com.nomi.caysenda.extension.model.request.*;
import com.nomi.caysenda.extension.repositories.ExtensionProductRepository;
import com.nomi.caysenda.extension.services.ExtensionService;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.services.ProgressService;
import com.nomi.caysenda.utils.UploadFileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("/extention")
public class ExtensionControllerAPIImpl implements ExtensionControllerAPI {
    @Autowired
    ExtensionService extensionService;
    @Autowired
    ProductService productService;
    @Autowired
    ProgressService progressService;

    @Override
    public ResponseEntity<Map> findAllShop(Integer page, Integer pageSize, String keyword) {
        Map map = new HashMap();
        map.put("success",true);
        page = page==null?0:page;
        pageSize= pageSize==null?20:pageSize;

        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(Sort.Direction.DESC,"id"));
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (SecurityUtils.isEmployeeExtension(SecurityUtils.getPrincipal())){
            map.put("data",extensionService.findAllCriteria(keyword,userDetail.getUserId(),pageable));
        }else {
            map.put("data",extensionService.findAllCriteria(keyword,null,pageable));
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateInfoShop(ExtensionShopUpdateRequest updateRequest) {
        extensionService.updateQuickEdit(updateRequest);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> uploadToWeb(ExtensionUpToWeb extensionUpToWeb)  {
        try {
            extensionService.updateToWeb(extensionUpToWeb.getShopId(),extensionUpToWeb.getCategoryId(), extensionUpToWeb.getProviders(),null);
        } catch (ProductException exception) {

        }
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> findAllProduct(Integer page, Integer pageSize, Integer shopId, String keyword) {
        Map map = new HashMap();
        map.put("success",true);
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        Pageable pageable = PageRequest.of(page==null?0:page-1,pageSize==null?50:pageSize);
        if (SecurityUtils.isEmployeeExtension(SecurityUtils.getPrincipal())){
            if (shopId==null){
                if (keyword!=null){
                    map.put("data",extensionService.findAllByShop_UserExtensionShop_Id(userDetail.getUserId(),pageable));
                }else {
                    map.put("data",extensionService.findAllByShop_UserExtensionShop_Id(userDetail.getUserId(),pageable));
                }
            }else {
                if (keyword!=null){

                }else {
                    map.put("data",extensionService.findAllByShop_IdAndShop_UserExtensionShop_Id(shopId,userDetail.getUserId(),pageable));
                }

            }
        }else {
            if (shopId!=null){
                if (keyword!=null){
                    map.put("data",extensionService.findAllByShop_IdAndNameZhLikeOrStandardNameLike(shopId,keyword,pageable));
                }else {
                    map.put("data",extensionService.findAllProductByShopId(shopId,pageable));
                }

            }else {
                if (keyword!=null){
                    map.put("data",extensionService.findAllProduct(keyword,pageable));
                }else {
                    map.put("data",extensionService.findAllProduct(pageable));
                }

            }
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> bulkAction(ExtensionBulkActionRequest actionRequest) {
        switch (actionRequest.getEvent()){
            case "browse": System.out.println("browse"); break;
            case "cancelbrowse":System.out.println("cancelbrowse");break;
            case "remove":extensionService.deleteProductByIds(actionRequest.getIds());break;
            case "removeToWeb": extensionService.removeToWeb(actionRequest.getIds());break;
            default:
        }
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateProduct(ExtensionUpdateProductRequest productRequest) {
        Map map = new HashMap();
        map.put("success",true);
        extensionService.updateProduct(productRequest);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> createProduct(ExtensionRequest request) {
        Map map = new HashMap();
        extensionService.createProduct(request);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Map> createFormNomi(ExtensionRequest request) {
        extensionService.createFromProduct(request);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateProduct(ExtensionRequest request) {
        Map map = new HashMap();
        if (request!=null){
            switch (request.getTypeRunning()){
                case "update": extensionService.updateProduct(request,true,true);break;
                case "updateStock": extensionService.updateProduct(request,false,true);break;
                case "updatePrice": extensionService.updateProduct(request,true,false);break;
            }
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateRateAndFactor(Integer shopId, Long exchangeRate, Float factorDefault, Float factor1, Float factor2, Float factor3, Float factor4) {
        extensionService.updateRateAndFactory(shopId,exchangeRate,factorDefault,factor1,factor2,factor3,factor4);
        return ResponseEntity.ok(Map.of("success",true));
    }



    @Override
    public ResponseEntity<Map> isExists(String productLink) {
        Map map = new HashMap();
        map.put("success",extensionService.isExistsProduct(productLink));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> findProductById(Integer productId) {
        Map map = new HashMap();
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        ExtensionProductEntity productEntity;
        if (SecurityUtils.isEmployeeExtension(userDetail)){
            productEntity = extensionService.findByIdAndShop_UserExtensionShop_Id(productId,userDetail.getUserId());
        }else {
            productEntity = extensionService.findById(productId);
        }

       if (productEntity!=null){
           map.put("success",true);
           map.put("product",productEntity);
           map.put("variants",productEntity.getVariants());
           map.put("attributes",productEntity.getAttributes());
       }else {
           map.put("success",false);
       }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> findAllEmployee() {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",extensionService.findAllEmployee());
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> authorization(Integer userId, Integer shopId) {
        Map map = new HashMap();
        map.put("success",true);
        extensionService.authorization(userId, shopId);
        return ResponseEntity.ok(map);
    }

    public ResponseEntity<Map> exportExcel(Integer shopId, Integer translate, HttpServletRequest request, HttpServletResponse response){
        HttpHeaders responseHeader = new HttpHeaders();

        try {
            extensionService.generateExcel(shopId,translate!=null?(translate.equals(1)?true:false):false );
           return ResponseEntity.ok(Map.of("success",true));

        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.NOT_FOUND);

        }
    }

    @Override
    public ResponseEntity<Map> uploadExcel(MultipartHttpServletRequest request) {
        try {
            extensionService.updateFromExcel(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateSKU(Integer shopId, String sku) {
        Map map = new HashMap();
        ExtensionShopEntity shopEntity = extensionService.updateSKUShop(shopId,sku);
        if (shopEntity!=null){
            map.put("success",true);
        }else {
            map.put("success",false);
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> downloadImage(Integer shopId) throws FileNotFoundException {
        extensionService.downloadImage(shopId);
        return ResponseEntity.ok(Map.of("success",true));
    }


    @Override
    public ResponseEntity<Map> removeProgress() {
        final String PROGRESS_CODE = "EXTENSION_EXCEL";
        progressService.delete(PROGRESS_CODE);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> downloadImages(Integer shopId) throws IOException {
        File s = UploadFileUtils.getPath("static/shop");
        File file = new File(s.getPath()+"/images-"+shopId);
        if (file.exists()){
            file.mkdirs();
        }
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(file));
        List<ExtensionProductEntity> products = extensionService.findAllByShop(shopId);
        ExtensionShopEntity shopEntity = extensionService.findShopById(shopId);
        for (ExtensionProductEntity product:products){
            List<ExtensionVariantEntity> variants = extensionService.findAllVariantByProduct(product.getId());
            String skuProduct = product.getSku()!=null?product.getSku():shopEntity.getSku()+product.getId();
            for (String image:product.getGallery()){
                ZipEntry zipEntry = new ZipEntry(skuProduct+"/"+FilenameUtils.getName(image));
                zipOut.putNextEntry(zipEntry);
            }
        }
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteById(Integer shopId) {
        extensionService.deleteByShop(shopId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteByLink(String link) {
        extensionService.deleteByLink(link);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Autowired
    ExtensionProductRepository extensionProductRepository;

    @Override
    public ResponseEntity<Map> fix() {
        List<ExtensionProductEntity> productEntities = extensionProductRepository.findAllByCount();
        extensionProductRepository.deleteAll(productEntities);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> removeSKU(Integer id) {
        extensionService.removeSku(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> disableSynchronize(Integer productId) {
        extensionService.disableSynchronize(productId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteProductById(Integer productId) {
        extensionService.deleteProductById(productId);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getCart(Integer page, Integer pageSize, Integer id) {
        Map map = new HashMap();
        if (id!=null){
            map.put("data",extensionService.findCartById(id));
        }else {
            Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20,Sort.by(Sort.Direction.DESC,"id"));
            map.put("data",extensionService.findAllcart(pageable));
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> getOrder(String orderId) {
        Map map = new HashMap();
        map.put("success",true);
        map.put("data",extensionService.runAddToCart(orderId));
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateOrder(ExtensionOrder order) {
        Map map = new HashMap();
        map.put("success",true);
        extensionService.updateAddToCart(order);
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> fixPrice(Integer id) {
        extensionService.fixPrice(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> getUpdateProductInfo(Integer shopId) {
        ExtensionShopEntity extensionShopEntity = extensionService.findShopById(shopId);
        return ResponseEntity.ok(Map.of("success",true,"data",extensionShopEntity));
    }

    @Override
    public ResponseEntity<Map> updateProductInfo(UpdateProductInfo updateProductInfo) {
        extensionService.updateProductInfo(updateProductInfo);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateCurrencyRate(Long value) {
        extensionService.updateCurrencyRate(value);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateEnablePrice(Integer id, Boolean enable) {
        extensionService.updateEnablePrice(id,enable);
        return ResponseEntity.ok(Map.of("success",true));
    }
    @Autowired
    OrderService orderService;
    @Override
    public ResponseEntity<Map> getDataTracking(String orderCode) {
        Map map = new HashMap();
        map.put("success",true);
        if (orderCode!=null){
            List<TrackingOrderEntity> orderEntity = orderService.findTrackingById(orderCode);
            if (orderEntity!=null){
                map.put("data",orderEntity);
            }
        }else {
            map.put("data",orderService.findAllTrackingByStatus("SIGN"));
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> updateDataTracking(UpdateTrackingRequest trackingRequest) {
        orderService.updateTrackingOrder(trackingRequest);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> deleteOrder(Integer id) {
        Map map = new HashMap();
        map.put("success",true);
        extensionService.deleteCart(id);
        return ResponseEntity.ok(map);
    }
}
