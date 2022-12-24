package com.nomi.caysenda.extension.controller;

import com.nomi.caysenda.exceptions.product.ProductException;
import com.nomi.caysenda.extension.model.ExtensionOrder;
import com.nomi.caysenda.extension.model.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface ExtensionControllerAPI {
    @PostMapping()
    ResponseEntity<Map> createProduct(@RequestBody ExtensionRequest request);
    @PostMapping("/create")
    ResponseEntity<Map> createFormNomi(@RequestBody ExtensionRequest request);
    @PutMapping()
    ResponseEntity<Map> updateProduct(@RequestBody ExtensionRequest request);
    @GetMapping("/update-rate-and-factor")
    ResponseEntity<Map> updateRateAndFactor(@RequestParam("shopId") Integer shopId,
                                            @RequestParam("exchangeRate") Long exchangeRate,
                                            @RequestParam("factorDefault") Float factorDefault,
                                            @RequestParam("factor1") Float factor1,
                                            @RequestParam("factor2") Float factor2,
                                            @RequestParam("factor3") Float factor3,
                                            @RequestParam("factor4") Float factor4);
    @GetMapping("/shop")
    ResponseEntity<Map> findAllShop(@RequestParam(value = "page",required = false) Integer page,
                                    @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                    @RequestParam(value = "keyword",required = false) String keyword);
    @PutMapping("/shop")
    ResponseEntity<Map> updateInfoShop(@RequestBody ExtensionShopUpdateRequest updateRequest);
    @PostMapping("/shop/upload-to-web")
    ResponseEntity<Map> uploadToWeb(@RequestBody ExtensionUpToWeb extensionUpToWeb) throws ProductException;
    @GetMapping("/product")
    ResponseEntity<Map> findAllProduct(@RequestParam(value = "page",required = false) Integer page,
                                       @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                       @RequestParam(value = "shopId",required = false) Integer shopId,
                                       @RequestParam(value = "keyword",required = false) String keyword
    );
    @PostMapping("/bulkaction")
    ResponseEntity<Map> bulkAction(@RequestBody ExtensionBulkActionRequest actionRequest);
    @PostMapping("/product")
    ResponseEntity<Map>  updateProduct(@RequestBody ExtensionUpdateProductRequest productRequest);
    @GetMapping("/isexists")
    ResponseEntity<Map> isExists(@RequestParam("url") String productLink);
    @GetMapping("/findproductbyid")
    ResponseEntity<Map> findProductById(@RequestParam("id") Integer productId);
    @GetMapping("/employee")
    ResponseEntity<Map> findAllEmployee();
    @PostMapping("/employee")
    ResponseEntity<Map> authorization(@RequestParam("userId") Integer userId,
                                      @RequestParam("shopId") Integer shopId);
    @GetMapping("/export-excel")
    ResponseEntity<Map> exportExcel(@RequestParam("shopId") Integer shopId,
                                    @RequestParam(value = "translate",required = false) Integer translate,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response);
    @PostMapping("/export-excel")
    ResponseEntity<Map> uploadExcel(MultipartHttpServletRequest request);

    @GetMapping("/update-sku")
    ResponseEntity<Map> updateSKU(@RequestParam("shopId") Integer shopId,
                                  @RequestParam("sku") String sku);
    @GetMapping("/image-processing")
    ResponseEntity<Map> downloadImage(@RequestParam("shopId") Integer shopId) throws FileNotFoundException;
    @GetMapping("/remove-progress")
    ResponseEntity<Map> removeProgress();
    @GetMapping("/download-images")
    ResponseEntity<Map> downloadImages(@RequestParam("shopId") Integer shopId) throws IOException;
    @GetMapping("/delete")
    ResponseEntity<Map> deleteById(@RequestParam("shopId") Integer shopId);
    @GetMapping("/deleteByLink")
    ResponseEntity<Map> deleteByLink(@RequestParam("link") String link);
    @GetMapping("/fix")
    ResponseEntity<Map> fix();
    @GetMapping("/remove-sku")
    ResponseEntity<Map> removeSKU(@RequestParam("shopId") Integer id);
    @GetMapping("/disable-synchronize")
    ResponseEntity<Map> disableSynchronize(@RequestParam("productId") Integer productId);
    @GetMapping("/deleteproductbyid")
    ResponseEntity<Map> deleteProductById(@RequestParam("productId") Integer productId);
    @GetMapping("/cart")
    ResponseEntity<Map> getCart(@RequestParam(value = "page",required = false) Integer page,
                                @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                @RequestParam(value = "id",required = false) Integer id);
    @DeleteMapping("/cart")
    ResponseEntity<Map> deleteOrder(@RequestParam("id") Integer id);

    @GetMapping("/addtocart")
    ResponseEntity<Map> getOrder(@RequestParam("orderId") String orderId);
    @PostMapping("/addtocart")
    ResponseEntity<Map> updateOrder(@RequestBody ExtensionOrder order);
    @GetMapping("/fix-price")
    ResponseEntity<Map> fixPrice(@RequestParam("id") Integer id);
    @GetMapping("/update-product-info")
    ResponseEntity<Map> getUpdateProductInfo(@RequestParam("id") Integer shopId);
    @PostMapping("/update-product-info")
    ResponseEntity<Map> updateProductInfo(@RequestBody UpdateProductInfo updateProductInfo);
    @GetMapping("/update-currency-rate")
    ResponseEntity<Map> updateCurrencyRate(@RequestParam("value") Long value);
    @GetMapping("/update-enableprice")
    ResponseEntity<Map> updateEnablePrice(@RequestParam("id") Integer id,@RequestParam("value") Boolean enable);
    @GetMapping("/get-data-tracking")
    ResponseEntity<Map> getDataTracking(@RequestParam(value = "id",required = false) String orderCode);
    @PostMapping("/get-data-tracking")
    ResponseEntity<Map> updateDataTracking(@RequestBody UpdateTrackingRequest trackingRequest);
}
