package com.nomi.caysenda.extension.services.impl;

import com.nomi.caysenda.api.admin.model.product.request.AdminProductRequest;
import com.nomi.caysenda.api.admin.model.product.request.AdminProductVariantRequest;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.exceptions.product.ProductException;
import com.nomi.caysenda.extension.dto.ExtensionOrderDTO;
import com.nomi.caysenda.extension.dto.ShopExtensionDTO;
import com.nomi.caysenda.extension.entity.*;
import com.nomi.caysenda.extension.model.*;
import com.nomi.caysenda.extension.model.request.*;
import com.nomi.caysenda.extension.repositories.*;
import com.nomi.caysenda.extension.services.ExtensionService;
import com.nomi.caysenda.repositories.CategoryRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.repositories.VariantRepository;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.ImageUtils;
import com.nomi.caysenda.utils.UploadFileUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackageRelationshipTypes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPictureNonVisual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service("extensionService")
public class ExtensionServiceImpl implements ExtensionService {
    @Autowired
    ExtensionShopRepository extentionShopRepository;
    @Autowired
    ExtensionProductRepository extentionProductRepository;
    @Autowired
    ExtensionVariantRepository extentionVariantRepository;
    @Autowired
    ExtensionAttributeRepository extensionAttributeRepository;
    @Autowired
    UserService userService;
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductService productService;
    @Autowired
    VariantRepository variantRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    ProgressService progressService;
    @Autowired
    Environment env;
    @Autowired
    LogService logService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderService orderService;
    final String PROGRESS_CODE = "EXTENSION_EXCEL";
    @Autowired
    ExtensionOrderRepository extensionOrderRepository;
    @Autowired
    ExtensionOrderDetailtRepository extensionOrderDetailtRepository;
    @Autowired
    DictionaryService dictionaryService;

    @Override
    public Page<ShopExtensionDTO> findAllCriteria(String keyword, Integer userId, Pageable pageable) {
        return extentionShopRepository.findAllCriteria(keyword, userId, pageable);
    }

    @Override
    public ExtensionShopEntity findShopById(Integer shopId) {
        return extentionShopRepository.findById(shopId).orElse(null);
    }

    @Override
    public Page<ShopExtensionDTO> findAllShop(Pageable pageable) {
        return extentionShopRepository.findAllShopExtensionDTO(pageable);
    }

    @Override
    public Page<ShopExtensionDTO> findAllByUserExtensionShop_Id(Integer userId, Pageable pageable) {
        return extentionShopRepository.findAllShopExtensionDTOByUserId(userId, pageable);
    }


    @Override
    public List<ExtensionProductEntity> findAllByShop(Integer shopId) {
        return extentionProductRepository.findAllByShop_Id(shopId);
    }

    @Override
    public Page<ExtensionProductEntity> findAllProduct(Pageable pageable) {
        return extentionProductRepository.findAll(pageable);
    }

    @Override
    public Page<ExtensionProductEntity> findAllProduct(String keyword, Pageable pageable) {
        String search = "%" + keyword + "%";
        return extentionProductRepository.findAllByNameZhLikeOrStandardNameLike(search, search, pageable);
    }

    @Override
    public Page<ExtensionProductEntity> findAllProductByShopId(Integer shopId, Pageable pageable) {
        return extentionProductRepository.findAllByShop_Id(shopId, pageable);
    }

    @Override
    public Page<ExtensionProductEntity> findAllByShop_IdAndNameZhLikeOrStandardNameLike(Integer shopId, String keyword, Pageable pageable) {
        return extentionProductRepository.findAllByShop_IdAndNameZhLikeOrStandardNameLike(shopId, keyword, keyword, pageable);
    }

    @Override
    public Page<ExtensionProductEntity> findAllByShop_UserExtensionShop_Id(Integer userId, Pageable pageable) {
        return extentionProductRepository.findAllByShop_UserExtensionShop_Id(userId, pageable);
    }

    @Override
    public Page<ExtensionProductEntity> findAllByShop_UserExtensionShop_Id(Integer userId, String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ExtensionProductEntity> findAllByShop_IdAndShop_UserExtensionShop_Id(Integer shopId, Integer userId, Pageable pageable) {
        return extentionProductRepository.findAllByShop_IdAndShop_UserExtensionShop_Id(shopId, userId, pageable);
    }

    @Override
    public List<ExtensionVariantEntity> findAllVariantByProduct(Integer productId) {
        return extentionVariantRepository.findAllByProduct_Id(productId);
    }

    @Override
    public void createProduct(ExtensionRequest extentionRequest) {
        ExtensionShopEntity extentionShopEntity = extentionShopRepository.findByLink(extentionRequest.getShopLink());
        if (extentionShopEntity == null) {
            ExtensionShopEntity shopEntity = new ExtensionShopEntity();
            shopEntity.setLink(extentionRequest.getShopLink());
            shopEntity.setName(extentionRequest.getShopLink());
            shopEntity.setExchangeRate(Long.valueOf(3700));
            shopEntity.setFactorDefault(Float.valueOf(String.valueOf(2.5)));
            shopEntity.setFactor1(Float.valueOf(String.valueOf(1.8)));
            shopEntity.setFactor2(Float.valueOf(String.valueOf(1.6)));
            shopEntity.setFactor3(Float.valueOf(String.valueOf(1.5)));
            shopEntity.setFactor4(Float.valueOf(String.valueOf(1.4)));
            extentionShopEntity = extentionShopRepository.save(shopEntity);
        }
        if (extentionProductRepository.findByLink(extentionRequest.getProduct().getLink()) != null) return;
        ExtensionProductEntity productEntity = new ExtensionProductEntity();
        productEntity.setNameZh(extentionRequest.getProduct().getNameZh());
        productEntity.setTempName(translate(extentionRequest.getProduct().getNameZh()));
        productEntity.setPrice1(extentionRequest.getProduct().getPrice1());
        if (extentionRequest.getProduct().getPrice2() != null) {
            productEntity.setPrice2(extentionRequest.getProduct().getPrice2());
        } else {
            productEntity.setPrice2(productEntity.getPrice1());
        }
        if (extentionRequest.getProduct().getPrice3() != null) {
            productEntity.setPrice3(extentionRequest.getProduct().getPrice3());
        } else {
            productEntity.setPrice3(productEntity.getPrice2());
        }
        productEntity.setVideo(extentionRequest.getProduct().getVideo());
        productEntity.setCondition1(extentionRequest.getProduct().getCondition1());
        productEntity.setCondition2(extentionRequest.getProduct().getCondition2());
        productEntity.setCondition3(extentionRequest.getProduct().getCondition3());
        productEntity.setGallery(extentionRequest.getProduct().getGallery());
        productEntity.setLink(extentionRequest.getProduct().getLink());
        productEntity.setShop(extentionShopEntity);
        productEntity.setQuantity(extentionRequest.getProduct().getQuantity());
        productEntity.setUnit(translate(extentionRequest.getProduct().getUnit()));
        productEntity.setUnitZh(extentionRequest.getProduct().getUnit());
        productEntity.setImages(extentionRequest.getProduct().getImages());
        productEntity = extentionProductRepository.save(productEntity);
        /** variants */
        for (ExtensionVariant extentionVariant : extentionRequest.getVariants()) {
            ExtensionVariantEntity variantEntity = new ExtensionVariantEntity();
            variantEntity.setNameZh(extentionVariant.getNameZh());
            variantEntity.setTempName(translate(extentionVariant.getNameZh()));
            variantEntity.setImg(extentionVariant.getImage());
            variantEntity.setPrice(extentionVariant.getPrice());
            variantEntity.setStock(extentionVariant.getStock());
            variantEntity.setUnit(extentionVariant.getUnit());
            variantEntity.setParent(extentionVariant.getParent());
            variantEntity.setParentTemp(translate(extentionVariant.getParent()));
            variantEntity.setProduct(productEntity);
            Double priceDefault, price1, price2, price3, price4 = Double.valueOf(0);
            if (productEntity.getPrice1() != null && productEntity.getPrice2() != null && productEntity.getPrice3() != null && productEntity.getPrice1() > productEntity.getPrice2() && productEntity.getPrice1() > productEntity.getPrice3()) {
                priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactorDefault().doubleValue();
                price1 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor1().doubleValue();
                price2 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice2() * extentionShopEntity.getFactor2().doubleValue();
                price3 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor3().doubleValue();
                price4 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor4().doubleValue();
            } else {
                priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * extentionVariant.getPrice() * extentionShopEntity.getFactorDefault().doubleValue();
                price1 = extentionShopEntity.getExchangeRate().doubleValue() * extentionVariant.getPrice() * extentionShopEntity.getFactor1().doubleValue();
                price2 = extentionShopEntity.getExchangeRate().doubleValue() * extentionVariant.getPrice() * extentionShopEntity.getFactor2().doubleValue();
                price3 = extentionShopEntity.getExchangeRate().doubleValue() * extentionVariant.getPrice() * extentionShopEntity.getFactor3().doubleValue();
                price4 = extentionShopEntity.getExchangeRate().doubleValue() * extentionVariant.getPrice() * extentionShopEntity.getFactor4().doubleValue();
            }

            variantEntity.setPriceDefault(round(priceDefault.longValue()));
            variantEntity.setPrice1(round(price1.longValue()));
            variantEntity.setPrice2(round(price2.longValue()));
            variantEntity.setPrice3(round(price3.longValue()));
            variantEntity.setPrice4(round(price4.longValue()));
            extentionVariantRepository.save(variantEntity);
        }
        /** attribute */
        if (extentionRequest.getAttributes()!=null){
            for (ExtensionAttribute extensionAttribute : extentionRequest.getAttributes()) {
                ExtensionAttributeEntity attributeEntity = new ExtensionAttributeEntity();
                attributeEntity.setName(extensionAttribute.getName());
                attributeEntity.setTempName(translate(extensionAttribute.getName()));
                attributeEntity.setValue(extensionAttribute.getValue());
                attributeEntity.setTempValue(translate(extensionAttribute.getValue()));
                attributeEntity.setProductAttribute(productEntity);
                extensionAttributeRepository.save(attributeEntity);
            }
        }

        String video = productEntity.getVideo();
        if (video != null && !video.equals("0")) {
            taskScheduler.schedule(() -> {
                downloadVideo(video);
            }, new Date());
        }
    }

    @Override
    public void updateProduct(ExtensionRequest extentionRequest, Boolean updatePrice, Boolean updateStock) {
        if (extentionRequest.getProduct().getGallery().size() > 0) {
            ExtensionShopEntity extentionShopEntity = extentionShopRepository.findByLink(extentionRequest.getShopLink());
            if (extentionShopEntity == null) {
                ExtensionShopEntity shopEntity = new ExtensionShopEntity();
                shopEntity.setLink(extentionRequest.getShopLink());
                shopEntity.setName(extentionRequest.getShopLink());
                shopEntity.setExchangeRate(Long.valueOf(3700));
                shopEntity.setFactorDefault(Float.valueOf(String.valueOf(2.5)));
                shopEntity.setFactor1(Float.valueOf(String.valueOf(1.8)));
                shopEntity.setFactor2(Float.valueOf(String.valueOf(1.6)));
                shopEntity.setFactor3(Float.valueOf(String.valueOf(1.5)));
                shopEntity.setFactor4(Float.valueOf(String.valueOf(1.4)));
                extentionShopEntity = extentionShopRepository.save(shopEntity);
            }
            ExtensionProductEntity productEntity = extentionProductRepository.findByLink(extentionRequest.getProduct().getLink());
            if (productEntity != null) {

                productEntity.setPrice1(extentionRequest.getProduct().getPrice1());
                if (extentionRequest.getProduct().getPrice2() != null) {
                    productEntity.setPrice2(extentionRequest.getProduct().getPrice2());
                } else {
                    productEntity.setPrice2(productEntity.getPrice1());
                }
                if (extentionRequest.getProduct().getPrice3() != null) {
                    productEntity.setPrice3(extentionRequest.getProduct().getPrice3());
                } else {
                    productEntity.setPrice3(productEntity.getPrice2());
                }
                productEntity.setCondition1(extentionRequest.getProduct().getCondition1());
                productEntity.setCondition2(extentionRequest.getProduct().getCondition2());
                productEntity.setCondition3(extentionRequest.getProduct().getCondition3());
                productEntity.setVideo(extentionRequest.getProduct().getVideo());
                productEntity.setShop(extentionShopEntity);
                productEntity.setQuantity(extentionRequest.getProduct().getQuantity());
                if (productEntity.getUnit() == null) {
                    productEntity.setUnit(translate(extentionRequest.getProduct().getUnit()));
                    productEntity.setUnitZh(extentionRequest.getProduct().getUnit());
                }
                productEntity.setImages(extentionRequest.getProduct().getImages());
                productEntity = extentionProductRepository.save(productEntity);
                productEntity.setVideo(extentionRequest.getProduct().getVideo());
                String video = productEntity.getVideo();
                if (video != null && !video.equals("0")) {
                    taskScheduler.schedule(() -> {
                        downloadVideo(video);
                    }, new Date());
                }
                /** variants */
                List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
                /** delete variant exists */
                Iterator<ExtensionVariantEntity> iteratorDeleteExists = variants.listIterator();
                while (iteratorDeleteExists.hasNext()) {
                    ExtensionVariantEntity variantEntity = iteratorDeleteExists.next();
                    Boolean isDelete = false;
                    Integer count = 0;
                    for (ExtensionVariantEntity entity : variants) {
                        if (variantEntity.getParent() != null && entity.getParent() != null && variantEntity.getNameZh() != null && entity.getNameZh() != null) {
                            if (variantEntity.getParent().equals(entity.getParent()) && variantEntity.getNameZh().equals(entity.getNameZh())) {
                                isDelete = true;
                                count++;
                            }
                        } else {
                            if (variantEntity.getNameZh() != null && entity.getNameZh() != null && variantEntity.getNameZh().equals(entity.getNameZh())) {
                                isDelete = true;
                                count++;
                            }
                        }
                    }
                    if (isDelete && count > 1) {
                        extentionVariantRepository.delete(variantEntity);
                        iteratorDeleteExists.remove();
                    }

                }
                /** delete variant */
                Iterator<ExtensionVariantEntity> iterator = variants.listIterator();
                while (iterator.hasNext()) {
                    Boolean isDelete = true;
                    ExtensionVariantEntity variantEntity = iterator.next();
                    for (ExtensionVariant extensionVariant : extentionRequest.getVariants()) {
                        if (variantEntity.getParent() != null && extensionVariant.getParent() != null && variantEntity.getNameZh() != null && extensionVariant.getNameZh() != null) {
                            if (variantEntity.getParent().equals(extensionVariant.getParent()) && variantEntity.getNameZh().equals(extensionVariant.getNameZh())) {
                                isDelete = false;
                            }
                        } else {
                            if (variantEntity.getNameZh() != null && extensionVariant.getNameZh() != null && variantEntity.getNameZh().equals(extensionVariant.getNameZh())) {
                                isDelete = false;
                            }
                        }
                    }
                    if (isDelete) {
                        extentionVariantRepository.delete(variantEntity);
                        iterator.remove();
                    }

                }

                for (ExtensionVariant extensionVariant : extentionRequest.getVariants()) {
                    ExtensionVariantEntity variantEntityUpdate = null;
                    List<ExtensionVariantEntity> variantEntities = new ArrayList<>();
                    if (extensionVariant.getParent() != null) {
                        variantEntities = variants.stream().filter(variantEntity ->
                                extensionVariant.getParent() != null &&
                                        variantEntity.getParent() != null &&
                                        extensionVariant.getParent().equals(variantEntity.getParent()) && variantEntity.getNameZh().equals(variantEntity.getNameZh())
                        ).collect(Collectors.toList());
                    } else {
                        variantEntities = variants.stream().filter(variantEntity -> variantEntity.getNameZh().equals(extensionVariant.getNameZh())).collect(Collectors.toList());
                    }

                    if (variantEntities.size() > 0) {
                        variantEntityUpdate = variantEntities.get(0);
                    } else {
                        variantEntityUpdate = new ExtensionVariantEntity();
                    }
                    variantEntityUpdate.setTempName(translate(extensionVariant.getNameZh()));
                    variantEntityUpdate.setNameZh(extensionVariant.getNameZh());
                    if (updateStock) {
                        variantEntityUpdate.setStock(extensionVariant.getStock());
                    }


                    variantEntityUpdate.setProduct(productEntity);
                    if (variantEntityUpdate.getParent() == null && extensionVariant.getParent() != null) {
                        variantEntityUpdate.setParent(extensionVariant.getParent());
                    }

                    if (productEntity.getSku() != null && variantEntityUpdate.getSku() == null) {
                        String variantSku = generateSkuVariant(productEntity.getId(), variantEntityUpdate.getParent());
                        variantEntityUpdate.setSku(variantSku);
                    }
                    if (updatePrice) {
                        variantEntityUpdate.setPrice(extensionVariant.getPrice());
                        Double priceDefault, price1, price2, price3, price4 = Double.valueOf(0);
                        if (productEntity.getPrice1() != null && productEntity.getPrice2() != null && productEntity.getPrice3() != null && productEntity.getPrice1() > productEntity.getPrice2() && productEntity.getPrice1() > productEntity.getPrice3()) {
                            priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactorDefault().doubleValue();
                            price1 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor1().doubleValue();
                            price2 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice2() * extentionShopEntity.getFactor2().doubleValue();
                            price3 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor3().doubleValue();
                            price4 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor4().doubleValue();
                        } else {
                            priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactorDefault().doubleValue();
                            price1 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor1().doubleValue();
                            price2 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor2().doubleValue();
                            price3 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor3().doubleValue();
                            price4 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor4().doubleValue();
                        }
                        variantEntityUpdate.setPriceDefault(round(priceDefault.longValue()));
                        variantEntityUpdate.setPrice1(round(price1.longValue()));
                        variantEntityUpdate.setPrice2(round(price2.longValue()));
                        variantEntityUpdate.setPrice3(round(price3.longValue()));
                        variantEntityUpdate.setPrice4(round(price4.longValue()));
                    }

                    /** update image */
                    if (variantEntityUpdate.getImg() == null && extensionVariant.getImage() != null) {
                        variantEntityUpdate.setImg(extensionVariant.getImage());
                    }
                    extentionVariantRepository.save(variantEntityUpdate);
                }

            } else {
                createProduct(extentionRequest);
            }
        }


    }

    private String generateSkuVariant(Integer productId, String parent) {
        List<ExtensionVariantEntity> variantEntities = extentionVariantRepository.findAllByProduct_Id(productId);
        List<ExtensionVariantEntity> sorted = variantEntities.stream().filter(variantEntity -> variantEntity.getSku() != null).sorted((o1, o2) -> {
            String[] so1 = o1.getSku().split("-");
            String[] so2 = o2.getSku().split("-");
            Integer integer1 = Integer.parseInt(so1[2]);
            Integer integer2 = Integer.parseInt(so2[2]);
            return integer1.compareTo(integer2);
        }).collect(Collectors.toList());
        List<ExtensionVariantEntity> filtered = parent == null ? new ArrayList<>() : sorted.stream().filter(variantEntity -> variantEntity.getParent().equals(parent)).collect(Collectors.toList());
        if (filtered.size() > 0) {
            ExtensionVariantEntity variantEntity = filtered.get(filtered.size() - 1);
            String[] s = variantEntity.getSku().split("-");
            s[3] = String.valueOf(Integer.parseInt(s[3]) + 1);
            return Arrays.stream(s).collect(Collectors.joining("-"));
        } else {
            ExtensionVariantEntity variantEntity = sorted.get(sorted.size() - 1);
            String[] s = variantEntity.getSku().split("-");
            if (s[3] != null) s[3] = String.valueOf(1);
            s[2] = String.valueOf(Integer.parseInt(s[2]) + 1);
            return Arrays.stream(s).collect(Collectors.joining("-"));
        }

    }

    @Override
    public void createFromProduct(ExtensionRequest extentionRequest) {
        ExtensionProductEntity productEntity = extentionProductRepository.findByLink(extentionRequest.getProduct().getLink());
        if (productEntity != null) {
            productEntity.setGallery(extentionRequest.getProduct().getGallery());
            productEntity.setNameZh(extentionRequest.getProduct().getNameZh());
            productEntity.setStandardName(extentionRequest.getProduct().getStandardName());
            productEntity.setTempName(extentionRequest.getProduct().getStandardName());
            productEntity.setLink(extentionRequest.getProduct().getLink());
            productEntity = extentionProductRepository.save(productEntity);
            /** variants */
            List<ExtensionVariantEntity> variantEntities = new ArrayList<>();
            if (productEntity.getId() != null) {
                variantEntities = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
            }

            for (ExtensionVariant extentionVariant : extentionRequest.getVariants()) {
                for (ExtensionVariantEntity variantEntity : variantEntities) {
                    if (extentionVariant.getNameZh().equals(variantEntity.getNameZh())) {
                        variantEntity.setParent(extentionVariant.getParent());
                        variantEntity.setProduct(productEntity);
                        variantEntity.setNameZh(extentionVariant.getNameZh());
                        variantEntity.setStandardName(extentionVariant.getStandardName());
                        variantEntity.setTempName(extentionVariant.getStandardName());
                        variantEntity.setImg(extentionVariant.getImage());
                        extentionVariantRepository.save(variantEntity);
                    }
                }
            }
        }

    }

    @Override
    public void deleteByShop(Integer shopId) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(shopId).orElse(null);
        if (shopEntity != null) {
            extentionShopRepository.delete(shopEntity);
        }

    }

    @Override
    public void deleteByLink(String link) {
        List<ExtensionProductEntity> products = extentionProductRepository.findAllByLink(link);
        List<ProductEntity> productEntities = productRepository.findAllByLink(link);
        if (productEntities.size() > 0) {
            productRepository.deleteAll(productEntities);
        }
        if (products.size() > 0) {
            extentionProductRepository.deleteAll(products);
        }
    }

    @Override
    public void deleteProductByIds(List<Integer> ids) {
        List<ExtensionProductEntity> products = extentionProductRepository.findAllById(ids);
        removeToWeb(ids);
        extentionProductRepository.deleteAll(products);

    }

    @Override
    public void deleteProductById(Integer id) {
        ExtensionProductEntity productEntity = extentionProductRepository.findById(id).orElse(null);
        if (productEntity != null) {
            extentionProductRepository.delete(productEntity);
        }
    }

    @Override
    public ExtensionProductEntity findById(Integer id) {
        Optional<ExtensionProductEntity> optional = extentionProductRepository.findById(id);
        if (optional != null) {
            ExtensionProductEntity productEntity = optional.get();
            List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
            List<ExtensionAttributeEntity> attributes = extensionAttributeRepository.findAllByProductAttribute_Id(productEntity.getId());
            productEntity.setAttributes(attributes);
            productEntity.setVariants(variants);
            return productEntity;
        }
        return null;
    }

    @Override
    public Boolean isExistsProduct(String productLink) {
        return extentionProductRepository.existsByLink(productLink);
    }

    @Override
    public void updateProduct(ExtensionUpdateProductRequest productRequest) {
        /** update product */
        ExtensionProductEntity productEntity = extentionProductRepository.findById(productRequest.getId()).get();
        if (productRequest.getStandardName() != null) productEntity.setStandardName(productRequest.getStandardName());
        if (productRequest.getDescription() != null) productEntity.setDescription(productRequest.getDescription());
        if (productRequest.getUnit() != null) productEntity.setUnit(productRequest.getUnit());
        extentionProductRepository.save(productEntity);
        /** end update product */

        /** update variant */
        if (productRequest.getVariants() != null && productRequest.getVariants().size() > 0) {
            for (ExtensionUpdateVariantRequest variantRequest : productRequest.getVariants()) {
                ExtensionVariantEntity variantEntity = extentionVariantRepository.findById(variantRequest.getId()).get();
                if (variantEntity != null) {
                    if (variantRequest.getStandardName() != null)
                        variantEntity.setStandardName(variantRequest.getStandardName());
                    if (variantRequest.getPrice() != null) variantEntity.setPrice(variantRequest.getPrice());
                    if (variantRequest.getPriceDefault() != null)
                        variantEntity.setPriceDefault(variantRequest.getPriceDefault());
                    if (variantRequest.getStock() != null) variantEntity.setStock(variantRequest.getStock());
                    if (variantRequest.getPrice1() != null) variantEntity.setPrice1(variantRequest.getPrice1());
                    if (variantRequest.getPrice2() != null) variantEntity.setPrice2(variantRequest.getPrice2());
                    if (variantRequest.getPrice3() != null) variantEntity.setPrice3(variantRequest.getPrice3());
                    if (variantRequest.getPrice4() != null) variantEntity.setPrice4(variantRequest.getPrice4());
                    if (variantRequest.getLength() != null) variantEntity.setLength(variantRequest.getLength());
                    if (variantRequest.getWidth() != null) variantEntity.setWidth(variantRequest.getWidth());
                    if (variantRequest.getHeight() != null) variantEntity.setHeight(variantRequest.getHeight());
                    if (variantRequest.getWeight() != null) variantEntity.setWeight(variantRequest.getWeight());
                    if (variantRequest.getDimension() != null)
                        variantEntity.setDimension(variantRequest.getDimension());

                    extentionVariantRepository.save(variantEntity);
                }
            }
        }
        /** end update variant */
    }

    @Override
    public ExtensionProductEntity findByIdAndShop_UserExtensionShop_Id(Integer id, Integer userId) {
        ExtensionProductEntity productEntity = extentionProductRepository.findByIdAndShop_UserExtensionShop_Id(id, userId);
        if (productEntity != null) {
            List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
            List<ExtensionAttributeEntity> attributes = extensionAttributeRepository.findAllByProductAttribute_Id(productEntity.getId());
            productEntity.setAttributes(attributes);
            productEntity.setVariants(variants);
            return productEntity;
        }
        return null;
    }

    @Override
    public List<UserEntity> findAllEmployee() {
        return userService.findAllByRole("ROLE_EMPLOYEE");
    }

    @Override
    public void authorization(Integer userId, Integer shopId) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(shopId).get();
        UserEntity userEntity = userService.findById(userId).get();
        shopEntity.setUserExtensionShop(userEntity);
        extentionShopRepository.save(shopEntity);
    }

    @Override
    public ExtensionShopEntity updateSKUShop(Integer shopId, String sku) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(shopId).orElse(null);
        if (shopEntity != null) {
            shopEntity.setSku(sku);
            List<ExtensionProductEntity> product = extentionProductRepository.findAllByShop_Id(shopId);
            for (ExtensionProductEntity extensionProductEntity : product) {
                if (extensionProductEntity.getSku() != null) {
                    extensionProductEntity.setSku(sku + extensionProductEntity.getSku().substring(extensionProductEntity.getSku().indexOf("-")));
                    ProductEntity productEntity = productService.findBySkuVi(extensionProductEntity.getSku());
                    List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(extensionProductEntity.getId());
                    if (productEntity != null) {
                        productEntity.setSkuZh("ZH" + sku + productEntity.getSkuZh().substring(productEntity.getSkuZh().indexOf("-")));
                        productEntity.setSkuVi(sku + productEntity.getSkuVi().substring(productEntity.getSkuVi().indexOf("-")));
                        if (extensionProductEntity.getQuantity() != null && extensionProductEntity.getQuantity() != 0) {
                            VariantEntity variantEntity = variantRepository.findBySkuVi(extensionProductEntity.getSku());
                            if (variantEntity != null) {
                                variantEntity.setSkuZh(sku + variantEntity.getSkuZh().substring(variantEntity.getSkuZh().indexOf("-")));
                                variantEntity.setSkuVi(sku + variantEntity.getSkuVi().substring(variantEntity.getSkuVi().indexOf("-")));
                                variantRepository.save(variantEntity);
                            }
                        } else {
                            for (ExtensionVariantEntity extensionVariantEntity : variants) {
                                VariantEntity variantEntity = variantRepository.findBySkuVi(extensionVariantEntity.getSku());
                                if (variantEntity != null) {
                                    variantEntity.setSkuZh(sku + variantEntity.getSkuZh().substring(variantEntity.getSkuZh().indexOf("-")));
                                    variantEntity.setSkuVi(sku + variantEntity.getSkuVi().substring(variantEntity.getSkuVi().indexOf("-")));
                                    variantRepository.save(variantEntity);
                                    extensionVariantEntity.setSku(variantEntity.getSkuVi());
                                    extentionVariantRepository.save(extensionVariantEntity);

                                }
                            }
                        }
                        productRepository.save(productEntity);
                        /** update extension */
                        extensionProductEntity.setSku(productEntity.getSkuVi());
                        extentionProductRepository.save(extensionProductEntity);

                    } else {
                        extentionProductRepository.save(extensionProductEntity);
                        for (ExtensionVariantEntity extensionVariantEntity : variants) {
                            if (extensionVariantEntity.getSku() != null) {
                                extensionVariantEntity.setSku(sku + extensionVariantEntity.getSku().substring(extensionVariantEntity.getSku().indexOf("-")));
                                extentionVariantRepository.save(extensionVariantEntity);
                            }
                        }
                    }
                }

            }

            return extentionShopRepository.save(shopEntity);
        }
        return null;
    }

    @Override
    public void updateRateAndFactory(Integer shopId, Long rate, Float factorDefault, Float facto1, Float factor2, Float facto3, Float factor4) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(shopId).orElse(null);
        shopEntity.setExchangeRate(rate);
        shopEntity.setFactorDefault(factorDefault);
        shopEntity.setFactor1(facto1);
        shopEntity.setFactor2(factor2);
        shopEntity.setFactor3(facto3);
        shopEntity.setFactor4(factor4);
        extentionShopRepository.save(shopEntity);
        if (shopEntity != null) {
            List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(shopId);
            taskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    products.forEach(extensionProductEntity -> {
                        Double productPrice1 = extensionProductEntity.getPrice1();
                        Double productPrice2 = extensionProductEntity.getPrice2() != null ? extensionProductEntity.getPrice2() : productPrice1;
                        Double productPrice3 = extensionProductEntity.getPrice3() != null ? extensionProductEntity.getPrice3() : productPrice2;
                        List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(extensionProductEntity.getId());
                        variants.forEach(extensionVariantEntity -> {
                            Double priceDefault, price1, price2, price3, price4 = Double.valueOf(0);
                            if (productPrice1 != null && productPrice2 != null && productPrice3 != null && productPrice1 > productPrice2 && productPrice1 > productPrice3) {
                                priceDefault = shopEntity.getExchangeRate().doubleValue() * productPrice1 * shopEntity.getFactorDefault().doubleValue();
                                price1 = shopEntity.getExchangeRate().doubleValue() * productPrice1 * shopEntity.getFactor1().doubleValue();
                                price2 = shopEntity.getExchangeRate().doubleValue() * productPrice2 * shopEntity.getFactor2().doubleValue();
                                price3 = shopEntity.getExchangeRate().doubleValue() * productPrice3 * shopEntity.getFactor3().doubleValue();
                                price4 = shopEntity.getExchangeRate().doubleValue() * productPrice3 * shopEntity.getFactor4().doubleValue();
                            } else {
                                priceDefault = shopEntity.getExchangeRate().doubleValue() * extensionVariantEntity.getPrice() * shopEntity.getFactorDefault().doubleValue();
                                price1 = shopEntity.getExchangeRate().doubleValue() * extensionVariantEntity.getPrice() * shopEntity.getFactor1().doubleValue();
                                price2 = shopEntity.getExchangeRate().doubleValue() * extensionVariantEntity.getPrice() * shopEntity.getFactor2().doubleValue();
                                price3 = shopEntity.getExchangeRate().doubleValue() * extensionVariantEntity.getPrice() * shopEntity.getFactor3().doubleValue();
                                price4 = shopEntity.getExchangeRate().doubleValue() * extensionVariantEntity.getPrice() * shopEntity.getFactor4().doubleValue();
                            }
                            extensionVariantEntity.setPriceDefault(round(priceDefault.longValue()));
                            extensionVariantEntity.setPrice1(round(price1.longValue()));
                            extensionVariantEntity.setPrice2(round(price2.longValue()));
                            extensionVariantEntity.setPrice3(round(price3.longValue()));
                            extensionVariantEntity.setPrice4(round(price4.longValue()));
                            extentionVariantRepository.save(extensionVariantEntity);
                        });


                    });
                }
            }, new Date());

        }
    }

    @Override
    public void generateExcel(Integer shopId, Boolean translate) throws FileNotFoundException {
        ExtensionShopEntity extensionShopEntity = extentionShopRepository.findById(shopId).get();
        if (extensionShopEntity.getSku() == null) return;
        List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(shopId);
        progressService.save(0, 100, PROGRESS_CODE);

        taskScheduler.schedule(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                File s = UploadFileUtils.getPath("static/");
                File folderExcel = new File(s.getPath() + "/excel");
                if (!folderExcel.exists()) folderExcel.mkdirs();
                FileOutputStream outputStream = new FileOutputStream(folderExcel.getPath() + "/" + shopId + ".xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet outSheet = workbook.createSheet("SP");
                try {
                    generateHeader(outSheet.createRow(0));
                    int i = 1;
                    int productId = 1;
                    for (ExtensionProductEntity productEntity : products) {
                        if (productEntity.getSku() == null) {
                            String sku = getSKU(extensionShopEntity.getSku(), productId);
                            productEntity.setSku(sku);
                            extentionProductRepository.save(productEntity);
                        }
                        List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
                        Map<String, List<ExtensionVariantEntity>> group = variants.stream().filter(variantEntity -> variantEntity.getParent() != null).collect(groupingBy(ExtensionVariantEntity::getParent));
                        if (variants.size() > 0) {
                            if (group.size() > 0) {
                                Iterator<Map.Entry<String, List<ExtensionVariantEntity>>> iterator = group.entrySet().iterator();
                                Integer variantIndex = 1;
                                while (iterator.hasNext()) {
                                    Map.Entry<String, List<ExtensionVariantEntity>> entry = iterator.next();
                                    Integer childIndex = 1;
                                    for (ExtensionVariantEntity variantEntity : entry.getValue()) {
                                        generateContentExcel(i++, productEntity.getSku(), workbook, outSheet, extensionShopEntity, productEntity, variantEntity, variantIndex, childIndex++, entry.getKey(), null, translate);
                                    }
                                    variantIndex++;
                                }
                            } else {
                                Integer variantIndex = 1;
                                for (ExtensionVariantEntity variantEntity : variants) {
                                    generateContentExcel(i++, productEntity.getSku(), workbook, outSheet, extensionShopEntity, productEntity, variantEntity, variantIndex++, null, null, null, translate);
                                }
                            }
                        } else {
                            if (productEntity.getQuantity() != null && productEntity.getQuantity() > 0) {
                                ExtensionVariantEntity variantEntity = new ExtensionVariantEntity();
                                variantEntity.setProduct(productEntity);
                                Double priceDefault, price1, price2, price3, price4 = Double.valueOf(0);
                                if (productEntity.getPrice1() != null || productEntity.getPrice2() != null || productEntity.getPrice3() != null) {
                                    priceDefault = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extensionShopEntity.getFactorDefault().doubleValue();
                                    price1 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extensionShopEntity.getFactor1().doubleValue();
                                    price2 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice2() * extensionShopEntity.getFactor2().doubleValue();
                                    price3 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extensionShopEntity.getFactor3().doubleValue();
                                    price4 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extensionShopEntity.getFactor4().doubleValue();
                                } else {
                                    priceDefault = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extensionShopEntity.getFactorDefault().doubleValue();
                                    price1 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extensionShopEntity.getFactor1().doubleValue();
                                    price2 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice2() * extensionShopEntity.getFactor2().doubleValue();
                                    price3 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extensionShopEntity.getFactor3().doubleValue();
                                    price4 = extensionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extensionShopEntity.getFactor4().doubleValue();
                                }
                                variantEntity.setPriceDefault(round(priceDefault.longValue()));
                                variantEntity.setPrice1(round(price1.longValue()));
                                variantEntity.setPrice2(round(price2.longValue()));
                                variantEntity.setPrice3(round(price3.longValue()));
                                variantEntity.setPrice4(round(price4.longValue()));
                                variantEntity.setNameZh(productEntity.getNameZh());
                                variantEntity.setStandardName(productEntity.getStandardName());
                                variantEntity.setTempName(productEntity.getTempName());
                                variantEntity.setStock(productEntity.getQuantity());
                                variantEntity.setPrice(productEntity.getPrice1());
                                variantEntity.setUnit(productEntity.getUnit());
                                generateContentExcel(i++, productEntity.getSku(), workbook, outSheet, extensionShopEntity, productEntity, variantEntity, 1, null, null, null, translate);
                            }
                        }
                        System.out.println("generate excel running " + productId);
                        productId++;
                        progressService.save(productId - 1, products.size(), PROGRESS_CODE);
                    }
                    progressService.delete(PROGRESS_CODE);
                    workbook.write(outputStream);
                    outputStream.close();
                    workbook.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Date());

    }


    void generateContentExcel(Integer i, String productSku, XSSFWorkbook workbook, XSSFSheet outSheet,
                              ExtensionShopEntity extensionShopEntity, ExtensionProductEntity productEntity,
                              ExtensionVariantEntity variantEntity, Integer variantIndex, Integer childIndex,
                              String parent, String parentTemp, Boolean translate) throws Exception {

        Row row = outSheet.createRow(i);
        row.createCell(new CellAddress("B" + i).getColumn()).setCellValue(extensionShopEntity.getSku());
        row.createCell(new CellAddress("C" + i).getColumn()).setCellValue(productSku);
        String sku = null;
        if (variantEntity.getSku() != null) {
            String[] values = variantEntity.getSku().split("-");
            if (values.length > 2) {
                row.createCell(new CellAddress("D" + i).getColumn()).setCellValue(extensionShopEntity.getSku() + "-" + values[1] + "-" + values[2]);
                row.createCell(new CellAddress("H" + i).getColumn()).setCellValue(variantEntity.getNameZh());
                if (translate) {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(translate(variantEntity.getNameZh()));
                } else {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(variantEntity.getStandardName() != null ? variantEntity.getStandardName() : variantEntity.getTempName());
                }
            }
            if (values.length > 3) {
                row.createCell(new CellAddress("H" + i).getColumn()).setCellValue(variantEntity.getParent());
                if (translate) {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(translate(variantEntity.getParent()));
                    row.createCell(new CellAddress("K" + i).getColumn()).setCellValue(translate(variantEntity.getNameZh()));
                } else {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(variantEntity.getParentTemp() != null ? variantEntity.getParentTemp() : variantEntity.getParent());
                    row.createCell(new CellAddress("K" + i).getColumn()).setCellValue(variantEntity.getStandardName() != null ? variantEntity.getStandardName() : variantEntity.getTempName());
                }
                row.createCell(new CellAddress("J" + i).getColumn()).setCellValue(variantEntity.getNameZh());
                row.createCell(new CellAddress("E" + i).getColumn()).setCellValue(extensionShopEntity.getSku() + "-" + values[1] + "-" + values[2] + "-" + values[3]);
            }

        } else {
            if (variantIndex != null) {
                sku = productSku + "-" + variantIndex;
                row.createCell(new CellAddress("D" + i).getColumn()).setCellValue(productSku + "-" + variantIndex);
                row.createCell(new CellAddress("H" + i).getColumn()).setCellValue(variantEntity.getNameZh());
                if (translate) {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(translate(variantEntity.getNameZh()));

                } else {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(variantEntity.getStandardName() != null ? variantEntity.getStandardName() : variantEntity.getTempName());
                }
            }
            if (childIndex != null) {
                row.createCell(new CellAddress("H" + i).getColumn()).setCellValue(variantEntity.getParent());
                row.createCell(new CellAddress("J" + i).getColumn()).setCellValue(variantEntity.getNameZh());
                row.createCell(new CellAddress("E" + i).getColumn()).setCellValue(productSku + "-" + variantIndex + "-" + childIndex);
                if (translate) {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(translate(variantEntity.getParent()));
                    row.createCell(new CellAddress("K" + i).getColumn()).setCellValue(translate(variantEntity.getNameZh()));
                } else {
                    row.createCell(new CellAddress("I" + i).getColumn()).setCellValue(variantEntity.getParentTemp() != null ? variantEntity.getParentTemp() : variantEntity.getParent());
                    row.createCell(new CellAddress("K" + i).getColumn()).setCellValue(variantEntity.getStandardName() != null ? variantEntity.getStandardName() : variantEntity.getTempName());
                }
                sku = productSku + "-" + variantIndex + "-" + childIndex;
            }
        }
        if (sku != null) {
            variantEntity.setSku(sku);
            extentionVariantRepository.save(variantEntity);
        }

        /** img */
        String url = null;
        if (variantEntity.getImg() != null && !variantEntity.getImg().equals("")) {
            url = variantEntity.getImg();
        } else {
            if (productEntity.getGallery().size() > 0) {
                url = productEntity.getGallery().get(0);
            }
        }
        if (url != null) {
            Picture picture = addPicture(url, row, workbook, outSheet);
            if (picture != null) {
                setHyperlinkToPicture(picture, productEntity.getLink());
            }

        }
        /** end img */

        Boolean checkUpdateVariant = false;

        if (checkUpdateVariant) {
            extentionVariantRepository.save(variantEntity);
        }
        row.createCell(new CellAddress("F" + i).getColumn()).setCellValue(productEntity.getNameZh());
        row.createCell(new CellAddress("G" + i).getColumn()).setCellValue(productEntity.getStandardName() != null ? productEntity.getStandardName() : productEntity.getTempName());
        Double price1, price2, price3 = null;
        price1 = variantEntity.getPrice() != null ? variantEntity.getPrice() : productEntity.getPrice1();//productEntity.getPrice1()!=null?productEntity.getPrice1():variantEntity.getPrice();
        price2 = productEntity.getPrice2() != null ? productEntity.getPrice2() : price1;
        price3 = productEntity.getPrice3() != null ? productEntity.getPrice3() : price2;
        row.createCell(new CellAddress("N" + i).getColumn()).setCellValue(price1);
        if (!price1.equals(price2) && !price2.equals(price3)){
            if (price1 > price2) {
                row.createCell(new CellAddress("O" + i).getColumn()).setCellValue(price2);
            }
            if (price1 > price3) {
                row.createCell(new CellAddress("P" + i).getColumn()).setCellValue(price3);
            }
        }
        row.createCell(new CellAddress("Q" + i).getColumn()).setCellValue(productEntity.getUnit() != null ? productEntity.getUnit() : "Cái");
        if (productEntity.getCondition1() != null)
            row.createCell(new CellAddress("R" + i).getColumn()).setCellValue(productEntity.getCondition1());
        if (productEntity.getCondition2() != null)
            row.createCell(new CellAddress("S" + i).getColumn()).setCellValue(productEntity.getCondition2());
        if (productEntity.getCondition3() != null)
            row.createCell(new CellAddress("T" + i).getColumn()).setCellValue(productEntity.getCondition3());
        if (variantEntity.getPrice() != null)
            row.createCell(new CellAddress("U" + i).getColumn()).setCellValue(variantEntity.getPrice() * extensionShopEntity.getExchangeRate());
        row.createCell(new CellAddress("V" + i).getColumn()).setCellValue(extensionShopEntity.getExchangeRate());
        row.createCell(new CellAddress("W" + i).getColumn()).setCellValue(variantEntity.getStock()!=null?variantEntity.getStock():0);
        row.createCell(new CellAddress("X" + i).getColumn()).setCellValue(variantEntity.getPriceDefault()!=null?variantEntity.getPriceDefault():0);
        row.createCell(new CellAddress("Y" + i).getColumn()).setCellValue(variantEntity.getPrice1()!=null?variantEntity.getPrice1():0);
        row.createCell(new CellAddress("Z" + i).getColumn()).setCellValue(variantEntity.getPrice2()!=null?variantEntity.getPrice2():0);
        row.createCell(new CellAddress("AA" + i).getColumn()).setCellValue(variantEntity.getPrice3()!=null?variantEntity.getPrice3():0);
        row.createCell(new CellAddress("AB" + i).getColumn()).setCellValue(variantEntity.getPrice4()!=null?variantEntity.getPrice4():0);
        row.createCell(new CellAddress("AC" + i).getColumn()).setCellValue(productEntity.getDescription()!=null?productEntity.getDescription():"");

        StringBuilder demesion = new StringBuilder("");
        if (variantEntity.getWeight() != null)
            row.createCell(new CellAddress("AD" + i).getColumn()).setCellValue(variantEntity.getWeight() != null ? variantEntity.getWeight() : 0);

        if (variantEntity.getWidth() != null)
            demesion.append(variantEntity.getWidth()).append("*");
        if (variantEntity.getHeight() != null)
            demesion.append(variantEntity.getHeight()).append("*");
        if (variantEntity.getLength() != null)
            demesion.append(variantEntity.getLength());
        row.createCell(new CellAddress("AE" + i).getColumn()).setCellValue(demesion.toString());
        if (productEntity.getGallery() != null) {
            if (productEntity.getGallery().size() > 0) {
                row.createCell(new CellAddress("AI" + i).getColumn()).setCellValue(productEntity.getGallery().get(0));
            }

        }
        row.createCell(new CellAddress("AJ" + i).getColumn()).setCellValue(url);
        List<ExtensionAttributeEntity> attributeEntities = extensionAttributeRepository.findAllByProductAttribute_Id(productEntity.getId());
        StringBuilder attributes = new StringBuilder("");
        attributeEntities.forEach(extensionAttributeEntity -> {
            attributes.append(extensionAttributeEntity.getTempName() + "(" + extensionAttributeEntity.getName() + ") - " + extensionAttributeEntity.getTempValue() + "(" + extensionAttributeEntity.getValue() + ")");
            attributes.append("\n");
        });
        row.createCell(new CellAddress("AG" + i).getColumn()).setCellValue(attributes.toString());
        row.createCell(new CellAddress("AK" + i).getColumn()).setCellValue(productEntity.getUnitZh() != null ? productEntity.getUnitZh() : "个");
        UrlValidator urlValidator = new UrlValidator();
        if (productEntity.getVideo() != null && !productEntity.getVideo().equals("") && !productEntity.getVideo().equals("0")) {
            if (urlValidator.isValid(productEntity.getVideo())) {
                if (!FilenameUtils.getBaseName(productEntity.getVideo()).equals("0")) {
                    row.createCell(new CellAddress("AH" + i).getColumn()).setCellValue(productEntity.getVideo());
                }
            } else {
                row.createCell(new CellAddress("AH" + i).getColumn()).setCellValue(env.getProperty("spring.domain") + "/resources/video/" + productEntity.getVideo() + ".mp4");
            }
        }

        row.createCell(new CellAddress("AL" + i).getColumn()).setCellValue(productEntity.getHtml());
        row.createCell(new CellAddress("AM" + i).getColumn()).setCellValue(productEntity.getKeyword() != null ? productEntity.getKeyword() : "");
    }

    @Override
    public void updateFromExcel(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        progressService.save(1, 100, PROGRESS_CODE);
        taskScheduler.schedule(() -> {
            while (fileNames.hasNext()) {
                String filename = fileNames.next();
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(filename);
                try {
                    InputStream inputStream = multipartFile.getInputStream();
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    int i = 1;
                    int rows = sheet.getLastRowNum();
                    while (i <= rows) {
                        Row row = sheet.getRow(i);
                        if (checkTypeExcel(row)) {
                            /** product */
                            String productSku = row.getCell(new CellAddress("C" + i).getColumn()).getStringCellValue();
                            ExtensionProductEntity productEntity = extentionProductRepository.findBySku(productSku);
                            if (productEntity != null) {
                                String productName = row.getCell(new CellAddress("G" + i).getColumn()) != null ? row.getCell(new CellAddress("G" + i).getColumn()).getStringCellValue() : null;
                                String unit = row.getCell(new CellAddress("Q" + i).getColumn()) != null ? row.getCell(new CellAddress("Q" + i).getColumn()).getStringCellValue() : "Cái";
                                String html = row.getCell(new CellAddress("AL" + i).getColumn()) != null ? row.getCell(new CellAddress("AL" + i).getColumn()).getStringCellValue() : "";
                                Double condition1 = row.getCell(new CellAddress("R" + i).getColumn()) != null ? (row.getCell(new CellAddress("R" + i).getColumn()).getNumericCellValue()) : null;
                                Double condition2 = row.getCell(new CellAddress("S" + i).getColumn()) != null ? (row.getCell(new CellAddress("S" + i).getColumn()).getNumericCellValue()) : null;
                                Double condition3 = row.getCell(new CellAddress("T" + i).getColumn()) != null ? (row.getCell(new CellAddress("T" + i).getColumn()).getNumericCellValue()) : null;
                                String description = row.getCell(new CellAddress("AC" + i).getColumn()) != null ? (row.getCell(new CellAddress("AC" + i).getColumn()).getStringCellValue()) : null;
                                String video = row.getCell(new CellAddress("AH" + i).getColumn()) != null ? (row.getCell(new CellAddress("AH" + i).getColumn()).getStringCellValue()) : null;
                                String keyword = row.getCell(new CellAddress("AM" + i).getColumn()) != null ? (row.getCell(new CellAddress("AM" + i).getColumn()).getStringCellValue()) : null;
                                productEntity.setKeyword(keyword);
                                productEntity.setStandardName(productName);
                                productEntity.setUnit(unit);
                                productEntity.setCondition1(condition1 != null ? condition1.intValue() : 0);
                                productEntity.setCondition2(condition2 != null ? condition2.intValue() : null);
                                productEntity.setCondition3(condition3 != null ? condition3.intValue() : null);
                                productEntity.setDescription(description);
                                productEntity.setHtml(html);
                                if (video != null && video.equals("")) {
                                    productEntity.setVideo(null);
                                } else {
                                    productEntity.setVideo(video);
                                }
                                productEntity = extentionProductRepository.save(productEntity);
                            }
                            /** variant*/
                            String variantSku = row.getCell(new CellAddress("D" + i).getColumn()).getStringCellValue();
                            String variantChildSku = row.getCell(new CellAddress("E" + i).getColumn()) != null ? row.getCell(new CellAddress("E" + i).getColumn()).getStringCellValue() : null;
                            ExtensionVariantEntity variantEntity = null;
                            System.out.println(variantSku);
                            if (variantChildSku != null && !variantChildSku.equals("")) {
                                variantEntity = extentionVariantRepository.findBySku(variantChildSku);
                            } else {
                                variantEntity = extentionVariantRepository.findBySku(variantSku);
                            }
                            if (variantEntity != null) {
                                /** variant data*/
                                String variantName = "";
                                if (row.getCell(new CellAddress("I" + i).getColumn()) != null) {
                                    if (row.getCell(new CellAddress("I" + i).getColumn()).getCellType().equals(CellType.NUMERIC)) {
                                        variantName = String.valueOf(row.getCell(new CellAddress("I" + i).getColumn()).getNumericCellValue());
                                    } else {
                                        variantName = row.getCell(new CellAddress("I" + i).getColumn()).getStringCellValue();
                                    }
                                }
                                String variantChildName = null;
                                if (row.getCell(new CellAddress("K" + i).getColumn()) != null) {
                                    if (row.getCell(new CellAddress("K" + i).getColumn()).getCellType().equals(CellType.NUMERIC)) {
                                        variantChildName = String.valueOf(row.getCell(new CellAddress("K" + i).getColumn()).getNumericCellValue());
                                    } else {
                                        variantChildName = row.getCell(new CellAddress("K" + i).getColumn()).getStringCellValue();
                                    }
                                }
                                Double stock = row.getCell(new CellAddress("W" + i).getColumn()) != null ? (row.getCell(new CellAddress("W" + i).getColumn()).getNumericCellValue()) : 0;
                                Double priceDefault = row.getCell(new CellAddress("X" + i).getColumn()) != null ? (row.getCell(new CellAddress("X" + i).getColumn()).getNumericCellValue()) : 0;
                                Double vip1 = row.getCell(new CellAddress("Y" + i).getColumn()) != null ? (row.getCell(new CellAddress("Y" + i).getColumn()).getNumericCellValue()) : 0;
                                Double vip2 = row.getCell(new CellAddress("Z" + i).getColumn()) != null ? (row.getCell(new CellAddress("Z" + i).getColumn()).getNumericCellValue()) : 0;
                                Double vip3 = row.getCell(new CellAddress("AA" + i).getColumn()) != null ? (row.getCell(new CellAddress("AA" + i).getColumn()).getNumericCellValue()) : 0;
                                Double vip4 = row.getCell(new CellAddress("AB" + i).getColumn()) != null ? (row.getCell(new CellAddress("AB" + i).getColumn()).getNumericCellValue()) : 0;
                                Double weight = row.getCell(new CellAddress("AD" + i).getColumn()) != null ? (row.getCell(new CellAddress("AD" + i).getColumn()).getNumericCellValue()) : 0;
                                String dimensionValue = row.getCell(new CellAddress("AE" + i).getColumn()) != null ? (row.getCell(new CellAddress("AE" + i).getColumn()).getStringCellValue()) : "";
                                String[] dimensions = dimensionValue.split("\\*");
                                if (dimensions.length > 0) {
                                    if (NumberUtils.isCreatable(dimensions[0])) {
                                        variantEntity.setWidth(Float.valueOf(dimensions[0]));
                                    }
                                }
                                if (dimensions.length > 1) {
                                    if (NumberUtils.isCreatable(dimensions[1])) {
                                        variantEntity.setHeight(Float.valueOf(dimensions[1]));
                                    }
                                }
                                if (dimensions.length > 2) {
                                    if (NumberUtils.isCreatable(dimensions[2])) {
                                        variantEntity.setLength(Float.valueOf(dimensions[2]));
                                    }
                                }
                                variantEntity.setDimension(dimensionValue);
                                variantEntity.setStock(stock.intValue());
                                variantEntity.setStandardName(variantName);
                                variantEntity.setPriceDefault(priceDefault.longValue());
                                variantEntity.setPrice1(vip1.longValue());
                                variantEntity.setPrice2(vip2.longValue());
                                variantEntity.setPrice3(vip3.longValue());
                                variantEntity.setPrice4(vip4.longValue());
                                variantEntity.setWeight(weight.floatValue());
                                if (variantChildSku != null && !variantChildSku.equals("")) {
                                    variantEntity.setParentTemp(variantName);
                                    variantEntity.setStandardName(variantChildName);
                                } else {
                                    variantEntity.setStandardName(variantName);
                                }
                                extentionVariantRepository.save(variantEntity);
                            }
                            progressService.save(i, rows, PROGRESS_CODE);
                        }
                        i++;
                    }
                    progressService.delete(PROGRESS_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Date());

    }

    private Boolean checkTypeExcel(Row row) {
        Boolean check = true;
        Integer i = row.getRowNum();
        CellType productName = row.getCell(new CellAddress("G" + i).getColumn()).getCellType();
        CellType variantName = row.getCell(new CellAddress("I" + i).getColumn()).getCellType();
        CellType weight = row.getCell(new CellAddress("AD" + i).getColumn()).getCellType();
        CellType dimension = row.getCell(new CellAddress("AE" + i).getColumn()) != null ? row.getCell(new CellAddress("AE" + i).getColumn()).getCellType() : null;
        StringBuilder builder = new StringBuilder("Sai dòng " + i);
        if (!productName.equals(CellType.STRING)) {
            builder.append(" cột " + "G");
            check = false;
        }
        if (!variantName.equals(CellType.STRING)) {
            builder.append(" cột " + "I");
            check = false;
        }
        if (!weight.equals(CellType.NUMERIC)) {
            builder.append(" cột " + "AD");
            check = false;
        }
        if (dimension != null && !dimension.equals(CellType.BLANK) && !dimension.equals(CellType.STRING)) {
            builder.append(" cột " + "AE");
            check = false;
        }
        if (!check) {
            logService.save(builder.toString(), "log_update_excel_extension");
        }
        return check;
    }

    private String getSKU(String shopSKU, Integer productId) {
        Boolean check = extentionProductRepository.existsBySku(shopSKU + "-" + productId);
        while (check) {
            check = extentionProductRepository.existsBySku(shopSKU + "-" + (++productId));
        }
        return shopSKU + "-" + productId;
    }

    @Override
    public void updateToWeb(Integer shopId, Integer categoryId, List<Integer> providerIds, Integer productId) {
        UrlValidator urlValidator = new UrlValidator();
        ExtensionProductEntity extensionProductEntity = null;
        if (productId != null) {
            extensionProductEntity = extentionProductRepository.findById(productId).orElse(null);
        }
        List<ExtensionProductEntity> products = extensionProductEntity != null ? List.of(extensionProductEntity) : extentionProductRepository.findAllByShop_Id(shopId);
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(shopId).orElse(null);
        List<CategoryEntity> parents = categoryService.getAllParent(categoryId);
        progressService.save(1, 100, PROGRESS_CODE);
        taskScheduler.schedule(() -> {
            if (shopEntity.getSku() != null) {
                Integer productIndex = 1;
                Integer size = products.size();
                for (ExtensionProductEntity product : products) {
                    if (product.getGallery() == null || product.getGallery().size() <= 0 || (product.getSynchronize() != null && !product.getSynchronize())) {
                        if (product.getSku() != null) {
                            ProductEntity productEntity = productRepository.findBySkuVi(product.getSku());
                            if (productEntity != null) productRepository.delete(productEntity);
                        }
                        continue;
                    } else {
                        List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(product.getId());
                        AdminProductRequest productRequest = new AdminProductRequest();
                        productRequest.setNameZh(product.getNameZh());
                        if (product.getSku() != null) {
                            productRequest.setSkuZh("ZH" + product.getSku());
                            productRequest.setSkuVi(product.getSku());
                        } else {
                            String sku = getSKU(shopEntity.getSku(), productIndex);
                            productRequest.setSkuZh("ZH" + sku);
                            productRequest.setSkuVi(sku);
                        }

                        if (product.getStandardName() != null) {
                            productRequest.setNameVi(product.getStandardName());
                        } else {
                            productRequest.setNameVi(product.getTempName());
                        }
                        productRequest.setThumbnail(product.getGallery().get(0));
                        productRequest.setGallery(product.getGallery());
                        productRequest.setStatus(1);
                        productRequest.setConditiondefault(product.getCondition1());
                        productRequest.setCondition1(product.getCondition2());
                        productRequest.setCondition2(product.getCondition3());
                        productRequest.setLink(product.getLink());
                        productRequest.setDescription(product.getDescription());
                        productRequest.setKeyword(product.getKeyword());
                        String content = generateContent(product.getImages()!=null && product.getImages().size()>0?product.getImages():product.getGallery());
                        productRequest.setContent(product.getHtml() != null ? product.getHtml() + content : content);
                        if (product.getCondition3() != null) productRequest.setCondition3(product.getCondition3() * 2);
                        if (product.getCondition3() != null) productRequest.setCondition4(product.getCondition3() * 4);
                        if (parents.size() > 0) {
                            List<Integer> categories = parents.stream().map(categoryEntity -> categoryEntity.getId()).collect(Collectors.toList());
                            categories.add(categoryId);
                            productRequest.setCategories(categories);
                        } else {
                            productRequest.setCategories(List.of(categoryId));
                        }
                        if (product.getCondition1() != null && product.getCondition2() != null && product.getCondition3() != null) {
                            productRequest.setRetail(false);
                        } else {
                            productRequest.setRetail(true);
                        }
                        if (product.getVideo() != null && !product.getVideo().equals("") && !product.getVideo().equals("0")) {
                            if (urlValidator.isValid(product.getVideo())) {
                                if (!FilenameUtils.getBaseName(product.getVideo()).equals("0")) {
                                    productRequest.setVideo(product.getVideo());
                                }
                            } else {
                                productRequest.setVideo(env.getProperty("spring.domain") + "/resources/video/" + product.getVideo() + ".mp4");
                            }
                        }

                        productRequest.setCategoryDefault(categoryId);
                        productRequest.setUnit(product.getUnit());
                        ProductEntity productEntity = productService.findBySkuVi(productRequest.getSkuVi());
                        if (productEntity != null) productRequest.setId(productEntity.getId());
                        List<AdminProductVariantRequest> variantsRequest = new ArrayList<>();
                        Boolean enable = false;
                        if (variants.size() > 0) {
                            Map<String, List<ExtensionVariantEntity>> group = variants.stream().filter(variantEntity -> variantEntity.getParent() != null).collect(groupingBy(ExtensionVariantEntity::getParent));
                            Iterator<Map.Entry<String, List<ExtensionVariantEntity>>> iterator = group.entrySet().iterator();
                            Integer variantIndex = 1;
                            if (group.size() > 0) {
                                while (iterator.hasNext()) {
                                    Map.Entry<String, List<ExtensionVariantEntity>> entry = iterator.next();
                                    Integer childIndex = 1;
                                    for (ExtensionVariantEntity variantEntity : entry.getValue()) {
                                        AdminProductVariantRequest variantRequest = generateVariantRequest(variantEntity, productEntity, shopEntity);
                                        if (variantRequest.getSkuVi() == null) {
                                            variantRequest.setSkuVi(productRequest.getSkuVi() + "-" + variantIndex + "-" + childIndex);
                                            variantRequest.setSkuZh(productRequest.getSkuZh() + "-" + variantIndex + "-" + childIndex);
                                            childIndex++;
                                        }
                                        VariantEntity productVariant = variantRepository.findBySkuVi(variantRequest.getSkuVi());
                                        if (productVariant != null) variantRequest.setId(productVariant.getId());
                                        variantsRequest.add(variantRequest);
                                        variantEntity.setSku(variantRequest.getSkuVi());
                                        /** save sku into extension */
                                        variantEntity.setSku(variantRequest.getSkuVi());
                                        extentionVariantRepository.save(variantEntity);
                                    }
                                    variantIndex++;
                                }
                            } else {
                                for (ExtensionVariantEntity variantEntity : variants) {
                                    AdminProductVariantRequest variantRequest = generateVariantRequest(variantEntity, productEntity, shopEntity);
                                    if (variantRequest.getSkuVi() == null) {
                                        variantRequest.setSkuVi(productRequest.getSkuVi() + "-" + variantIndex);
                                        variantRequest.setSkuZh(productRequest.getSkuZh() + "-" + variantIndex);
                                    }
                                    VariantEntity productVariant = variantRepository.findBySkuVi(variantRequest.getSkuVi());
                                    if (productVariant != null) variantRequest.setId(productVariant.getId());
                                    variantsRequest.add(variantRequest);

                                    variantEntity.setSku(variantRequest.getSkuVi());
                                    /** save sku into extension */
                                    variantEntity.setSku(variantRequest.getSkuVi());
                                    extentionVariantRepository.save(variantEntity);
                                    variantIndex++;
                                }
                            }
                        } else {
                            if (product.getQuantity() != null) {
                                AdminProductVariantRequest variantRequest = new AdminProductVariantRequest();
                                variantRequest.setNameZh(product.getNameZh());
                                variantRequest.setSkuZh(productRequest.getSkuZh() + "-1");
                                variantRequest.setSkuVi(productRequest.getSkuVi() + "-1");

                                if (product.getStandardName() != null) {
                                    variantRequest.setNameVi(productRequest.getNameVi());
                                } else {
                                    variantRequest.setNameVi(variantRequest.getSkuVi());
                                }
                                enable = true;
                                VariantEntity productVariant = variantRepository.findBySkuVi(variantRequest.getSkuVi());
                                if (productVariant != null) variantRequest.setId(productVariant.getId());
                                variantRequest.setThumbnail(product.getGallery().get(0));
                                variantRequest.setWeight(Float.valueOf(10));
                                variantRequest.setWidth(Float.valueOf(10));
                                variantRequest.setHeight(Float.valueOf(10));
                                variantRequest.setLength(Float.valueOf(10));
                                variantRequest.setStock(product.getQuantity());
                                variantRequest.setPriceZh(product.getPrice1());
                                variantsRequest.add(variantRequest);
                            }
                        }
                        productRequest.setProviders(providerIds);
                        productRequest.setEnableAutoUpdatePrice(enable);
                        productRequest.setVariants(variantsRequest);
                        if (product.getSku() == null) {
                            product.setSku(productRequest.getSkuVi());
                            extentionProductRepository.save(product);
                        }
                        if (productRequest.getVariants().size() > 0) {
                            if (productRequest.getId() == null) {
                                try {
                                    productService.create(productRequest);
                                } catch (ProductException exception) {
                                    logService.save("Sản phẩm tên:" + productRequest.getNameVi() + " bị lỗi" + "-" + exception.getMessage(), "log_update_web_extension");
                                    exception.printStackTrace();
                                }
                            } else {
                                productService.update(productRequest);
                            }
                        }
                    }
                    progressService.save(productIndex, size, PROGRESS_CODE);
                    productIndex++;
                }
                progressService.delete(PROGRESS_CODE);
            }
        }, new Date());

    }

    AdminProductVariantRequest generateVariantRequest(ExtensionVariantEntity variantEntity, ProductEntity productEntity, ExtensionShopEntity shopEntity) {
        AdminProductVariantRequest variantRequest = new AdminProductVariantRequest();
        variantRequest.setNameZh(variantEntity.getNameZh());
        if (variantEntity.getSku() != null) {
            variantRequest.setSkuZh("ZH" + variantEntity.getSku());
            variantRequest.setSkuVi(variantEntity.getSku());
        }
        if (variantEntity.getStandardName() != null) {
            variantRequest.setNameVi(variantEntity.getStandardName());
        } else {
            variantRequest.setNameVi(variantEntity.getTempName());
        }

        variantRequest.setThumbnail(variantEntity.getImg());
        variantRequest.setWeight(variantEntity.getWeight());
        variantRequest.setWidth(variantEntity.getWidth());
        variantRequest.setHeight(variantEntity.getHeight());
        variantRequest.setLength(variantEntity.getLength());
        variantRequest.setStock(variantEntity.getStock());
        variantRequest.setPrice(variantEntity.getPriceDefault());
        variantRequest.setVip1(variantEntity.getPrice1());
        variantRequest.setVip2(variantEntity.getPrice2());
        variantRequest.setVip3(variantEntity.getPrice3());
        variantRequest.setVip4(variantEntity.getPrice4());
        variantRequest.setPriceZh(variantEntity.getPrice());
        variantRequest.setParent(variantEntity.getParent());
        variantRequest.setParentTemp(variantEntity.getParentTemp());
        variantRequest.setDimension(variantEntity.getDimension());
        Double cost = variantEntity.getPrice() * shopEntity.getExchangeRate();
        variantRequest.setCost(cost.longValue());
        return variantRequest;
    }

    @Override
    public void removeToWeb(List<Integer> ids) {
        List<ExtensionProductEntity> products = extentionProductRepository.findAllById(ids);
        products.forEach(extensionProductEntity -> {
            ProductEntity productEntity = productService.findBySkuVi(extensionProductEntity.getSku());
            if (productEntity != null) {
                productService.delete(productEntity.getId());
            }

        });
    }

    @Override
    public void downloadImage(Integer shopId) throws FileNotFoundException {
        List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(shopId);
        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath() + "/upload");
        UrlValidator urlValidator = new UrlValidator();
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                AtomicReference<Integer> productIndex = new AtomicReference<>(1);
                products.forEach(extensionProductEntity -> {
                    if (extensionProductEntity.getImages() != null) {
                        List<String> images = new ArrayList<>();
                        extensionProductEntity.getImages().forEach(s -> {
                            if (urlValidator.isValid(s)) {
                                byte[] bytes = getInputImage(s);
                                if (bytes != null) {
                                    File file = new File(folder.getPath() + "/" + FilenameUtils.getName(s));
                                    try {
                                        BufferedImage bufferedImage = ImageUtils.compressImage(bytes);
                                        if (bufferedImage != null) {
                                            FileOutputStream outputStream = new FileOutputStream(file);
                                            ImageIO.write(bufferedImage, "jpg", outputStream);
                                            outputStream.close();
                                            images.add("/resources/upload/" + FilenameUtils.getName(s));
                                        }
                                    } catch (FileNotFoundException e) {

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                images.add(s);
                            }

                        });
                        extensionProductEntity.setImages(images);
                        extensionProductEntity = extentionProductRepository.save(extensionProductEntity);
                    }

                    if (extensionProductEntity.getGallery() != null) {
                        List<String> gallery = new ArrayList<>();
                        extensionProductEntity.getGallery().forEach(s -> {
                            if (urlValidator.isValid(s)) {
                                byte[] bytes = getInputImage(s);
                                if (bytes != null) {
                                    File file = new File(folder.getPath() + "/" + FilenameUtils.getName(s));
                                    try {
                                        BufferedImage bufferedImage = ImageUtils.cropAndResize(bytes);
                                        if (bufferedImage != null) {
                                            FileOutputStream outputStream = new FileOutputStream(file);
                                            ImageIO.write(bufferedImage, "jpg", outputStream);
                                            outputStream.close();
                                            gallery.add("/resources/upload/" + FilenameUtils.getName(s));
                                        }
                                    } catch (FileNotFoundException e) {

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                gallery.add(s);
                            }

                        });
                        extensionProductEntity.setGallery(gallery);
                        extentionProductRepository.save(extensionProductEntity);
                    }
                    List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(extensionProductEntity.getId());
                    variants.forEach(extensionVariantEntity -> {
                        if (extensionVariantEntity.getImg() != null) {
                            if (urlValidator.isValid(extensionVariantEntity.getImg())) {
                                byte[] bytes = getInputImage(extensionVariantEntity.getImg());
                                if (bytes != null) {
                                    File file = new File(folder.getPath() + "/" + FilenameUtils.getName(extensionVariantEntity.getImg()));
                                    try {
                                        BufferedImage bufferedImage = ImageUtils.cropAndResize(bytes);
                                        if (bufferedImage != null) {
                                            FileOutputStream outputStream = new FileOutputStream(file);
                                            ImageIO.write(bufferedImage, FilenameUtils.getExtension(extensionVariantEntity.getImg()), outputStream);
                                            outputStream.close();
                                            extensionVariantEntity.setImg("/resources/upload/" + FilenameUtils.getName(extensionVariantEntity.getImg()));
                                            extentionVariantRepository.save(extensionVariantEntity);
                                        }

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                extensionVariantEntity.setImg(extensionVariantEntity.getImg());
                            }
                        }

                    });
                    progressService.save(productIndex.get() - 1, products.size(), PROGRESS_CODE);
                    productIndex.getAndSet(productIndex.get() + 1);
                });
                progressService.delete(PROGRESS_CODE);
            }
        }, new Date());
    }

    @Override
    public void disableSynchronize(Integer productId) {
        ExtensionProductEntity productEntity = extentionProductRepository.findById(productId).orElse(null);
        if (productEntity != null) {
            productEntity.setSynchronize(productEntity.getSynchronize() != null ? !productEntity.getSynchronize() : false);
            extentionProductRepository.save(productEntity);

        }
    }

    @Override
    public void updateQuickEdit(ExtensionShopUpdateRequest updateRequest) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(updateRequest.getId()).orElse(null);
        if (shopEntity != null) {
            if (updateRequest.getName() != null) shopEntity.setName(updateRequest.getName());
            if (updateRequest.getMainProduct() != null) shopEntity.setMainProduct(updateRequest.getMainProduct());
            if (updateRequest.getStatus() != null) shopEntity.setStatus(updateRequest.getStatus());
            extentionShopRepository.save(shopEntity);
        }
    }

    @Override
    public void removeSku(Integer shopId) {
        List<ExtensionProductEntity> productEntities = extentionProductRepository.findAllByShop_Id(shopId);
        productEntities.forEach(productEntity -> {
            productEntity.setSku(null);
            List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());
            variants.forEach(extensionVariantEntity -> {
                extensionVariantEntity.setSku(null);
            });
            extentionVariantRepository.saveAll(variants);
            extentionProductRepository.save(productEntity);

        });
    }

    @Override
    public Page<ExtensionOrderDTO> findAllcart(Pageable pageable) {
        return extensionOrderRepository.findAllForDTO(pageable);
    }

    @Override
    public ExtensionOrderEntity findCartById(Integer id) {
        return extensionOrderRepository.findById(id).orElse(null);
    }

    @Override
    public ExtensionOrder runAddToCart(String orderId) {
        ExtensionOrderEntity orderEntity = null;
        if (orderId.indexOf("@") >= 0) {
            orderEntity = extensionOrderRepository.findById(Integer.parseInt(orderId.substring(orderId.indexOf("@") + 1, orderId.length()))).orElse(null);
        } else {
            orderEntity = getDataAddToCart(Integer.parseInt(orderId));
        }

        ExtensionOrder extensionOrder = new ExtensionOrder();
        extensionOrder.setOrderId(orderEntity.getId());
        extensionOrder.setFullName(orderEntity.getFullName());
        extensionOrder.setPhone(orderEntity.getPhoneNumber());
        Map<Integer, List<ExtensionOrderDetailtEntity>> productsGroup = orderEntity.getDetailts().stream().collect(Collectors.groupingBy(ExtensionOrderDetailtEntity::getProductId));
        List<ExtensionOrderProduct> products = new ArrayList<>();
        productsGroup.entrySet().forEach(integerListEntry -> {
            ExtensionOrderProduct orderProduct = new ExtensionOrderProduct();
            orderProduct.setName(integerListEntry.getValue().get(0).getProductName());
            orderProduct.setLink(integerListEntry.getValue().get(0).getLinkZh());
            orderProduct.setNameZh(integerListEntry.getValue().get(0).getNameZh());
            /** variants */
            List<ExtensionOrderVariant> variants = new ArrayList<>();
            integerListEntry.getValue().forEach(orderDetailtEntity -> {
                ExtensionOrderVariant orderVariant = new ExtensionOrderVariant();
                orderVariant.setId(orderDetailtEntity.getId());
                orderVariant.setGroup(orderDetailtEntity.getGroupZhName());
                orderVariant.setOrderedQuantity(0);
                orderVariant.setName(orderDetailtEntity.getVariantName());
                orderVariant.setNameZh(orderDetailtEntity.getVariantNameZh());
                orderVariant.setQuantity(orderDetailtEntity.getQuantity());
                variants.add(orderVariant);
            });
            orderProduct.setVariants(variants);
            products.add(orderProduct);
        });
        extensionOrder.setProducts(products);
        return extensionOrder;
    }

    @Override
    public ExtensionOrderEntity getDataAddToCart(Integer orderId) {
        ExtensionOrderEntity orderEntity = extensionOrderRepository.findByOrderIdAndSub(orderId, false);
        if (orderEntity != null) {
            return orderEntity;
        } else {
            List<ExtensionOrderEntity> orders = new ArrayList<>();
            OrderEntity order = orderService.findById(orderId);
            List<OrderDetailtEntity> detailts = orderService.findAllDetailtByOrderId(orderId);
            if (detailts.size() < 200) {
                orderEntity = new ExtensionOrderEntity();
                orderEntity.setOrderId(orderId);
                orderEntity.setFullName(order.getBillingFullName());
                orderEntity.setPhoneNumber(order.getBillingPhoneNumber());
                orderEntity.setSub(false);
                orderEntity = extensionOrderRepository.save(orderEntity);
                orders.add(orderEntity);
            } else {
                Integer count = detailts.size() / 200;
                for (int i = 0; i <= count; i++) {
                    ExtensionOrderEntity subOrder = new ExtensionOrderEntity();
                    subOrder.setOrderId(orderId);
                    subOrder.setFullName(order.getBillingFullName() + " - " + (i + 1));
                    subOrder.setPhoneNumber(order.getBillingPhoneNumber());
                    if (i > 0) {
                        subOrder.setSub(true);
                    } else {
                        subOrder.setSub(false);
                    }
                    subOrder = extensionOrderRepository.save(subOrder);
                    orders.add(subOrder);
                }
            }

            /** sub order */
            int i = 0;
            for (ExtensionOrderEntity extensionOrderEntity : orders) {
                extensionOrderEntity.setDetailts(new ArrayList<>());
                for (int j = i * 200; j < i * 200 + 200; j++) {
                    if (j>=detailts.size()) break;
                    OrderDetailtEntity orderDetailtEntity = detailts.get(j);

                    if (orderDetailtEntity.getProductId() != null && orderDetailtEntity.getVariantId() != null) {
                        ExtensionOrderDetailtEntity extensionOrderDetailt = new ExtensionOrderDetailtEntity();
                        extensionOrderDetailt.setOrderEntity(extensionOrderEntity);
                        extensionOrderDetailt.setProductName(orderDetailtEntity.getName());
                        extensionOrderDetailt.setVariantName(orderDetailtEntity.getVariantName());
                        extensionOrderDetailt.setProductThumbnail(orderDetailtEntity.getProductThumbnail());
                        extensionOrderDetailt.setVariantThumbnail(orderDetailtEntity.getVariantThumbnail());
                        extensionOrderDetailt.setProductId(orderDetailtEntity.getProductId());
                        extensionOrderDetailt.setVariantId(orderDetailtEntity.getVariantId());
                        extensionOrderDetailt.setLinkZh(orderDetailtEntity.getLinkZh());
                        extensionOrderDetailt.setNameZh(orderDetailtEntity.getNameZh());
                        extensionOrderDetailt.setVariantNameZh(orderDetailtEntity.getVariantNameZh());
                        extensionOrderDetailt.setGroupName(orderDetailtEntity.getGroupName());
                        extensionOrderDetailt.setGroupZhName(orderDetailtEntity.getGroupZhName());
                        extensionOrderDetailt.setGroupSku(orderDetailtEntity.getGroupSku());
                        extensionOrderDetailt.setQuantity(orderDetailtEntity.getOwe());
                        extensionOrderDetailt.setOrderedQuantity(0);
                        extensionOrderDetailt.setSku(orderDetailtEntity.getSku());
                        extensionOrderEntity.getDetailts().add(extensionOrderDetailtRepository.save(extensionOrderDetailt));
                    }
                }
                i++;

            }

            return orders.get(0);
        }

    }

    @Override
    public void updateAddToCart(ExtensionOrder extensionOrder) {
        extensionOrder.getProducts().forEach(extensionOrderProduct -> {
            extensionOrderProduct.getVariants().forEach(extensionOrderVariant -> {
                ExtensionOrderDetailtEntity orderDetailtEntity = extensionOrderDetailtRepository.findById(extensionOrderVariant.getId()).orElse(null);
                if (orderDetailtEntity != null) {
                    orderDetailtEntity.setOrderedQuantity(extensionOrderVariant.getOrderedQuantity());
                    extensionOrderDetailtRepository.save(orderDetailtEntity);
                }
            });
        });
    }

    @Override
    public void deleteCart(Integer id) {
        ExtensionOrderEntity orderEntity = extensionOrderRepository.findById(id).orElse(null);
        if (orderEntity != null) {
            extensionOrderRepository.delete(orderEntity);
        }
    }

    @Override
    public void fixPrice(Integer id) {
        List<ExtensionShopEntity> shops = extentionShopRepository.findAll();
        shops.forEach(extentionShopEntity -> {
            List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(extentionShopEntity.getId());
            products.forEach(productEntity -> {
                if (productEntity.getVideo() != null) {
                    String name = FilenameUtils.getName(productEntity.getVideo());
                    String[] namesplit = name.split("\\.");
                    if (namesplit[0] != null && namesplit[0].equals("0")) {
                        productEntity.setVideo(null);
                        if (productEntity.getSku() != null) {
                            ProductEntity productEntity1 = productRepository.findBySkuVi(productEntity.getSku());
                            if (productEntity1 != null) {
                                productEntity1.setVideo(null);
                                productRepository.save(productEntity1);
                            }
                        }
                        extentionProductRepository.save(productEntity);
                    }
                    if (namesplit.length >= 3) {
                        name = env.getProperty("spring.domain") + "/resources/video/" + namesplit[0] + "." + namesplit[1];
                        productEntity.setVideo(name);
                        if (productEntity.getSku() != null) {
                            ProductEntity productEntity1 = productRepository.findBySkuVi(productEntity.getSku());
                            if (productEntity1 != null) {
                                productEntity1.setVideo(name);
                                productRepository.save(productEntity1);
                            }
                        }
                        extentionProductRepository.save(productEntity);
                    }
                }
            });
        });

    }

    @Override
    public void updateProductInfo(UpdateProductInfo productInfo) {
        Optional<ExtensionShopEntity> extensionShopEntity = extentionShopRepository.findById(productInfo.getShopId());
        if (extensionShopEntity.isPresent()) {
            progressService.save(0, 100, PROGRESS_CODE);
            taskScheduler.schedule(() -> {
                ExtensionShopEntity shopEntity = extensionShopEntity.get();
                shopEntity.setShip(productInfo.getShip());
                shopEntity.setDescript(productInfo.getDescript());
                shopEntity.setHtml(productInfo.getHtml());
                shopEntity.setKeyword(productInfo.getKeyword());
                Double aDouble = Double.parseDouble(productInfo.getShip());
                extentionShopRepository.save(shopEntity);
                List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(productInfo.getShopId());
                AtomicReference<Integer> i = new AtomicReference<>(0);
                products.forEach(extensionProductEntity -> {
                    extensionProductEntity.setKeyword(productInfo.getKeyword());
                    extensionProductEntity.setHtml(productInfo.getHtml());
                    extensionProductEntity.setDescription(productInfo.getDescript());
                    extentionProductRepository.save(extensionProductEntity);
                    List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(extensionProductEntity.getId());
                    variants.forEach(extensionVariantEntity -> {
                        Double weight = extensionVariantEntity.getPrice() * shopEntity.getExchangeRate() * aDouble;
                        extensionVariantEntity.setWeight(weight.floatValue());
                        extentionVariantRepository.save(extensionVariantEntity);
                    });
                    progressService.save(i.getAndSet(i.get() + 1), products.size(), PROGRESS_CODE);
                });
                progressService.delete(PROGRESS_CODE);
            }, new Date());

        }
    }

    @Override
    public void updateCurrencyRate(Long currentRate) {
        List<ExtensionShopEntity> shops = extentionShopRepository.findAll();
        progressService.save(0, 100, PROGRESS_CODE);
        taskScheduler.schedule(() -> {
            Integer i = 0;
            for (ExtensionShopEntity extentionShopEntity : shops) {
                if (extentionShopEntity.getEnableUpdatePrice()) {
                    extentionShopEntity.setExchangeRate(currentRate);
                    extentionShopRepository.save(extentionShopEntity);
                    List<ExtensionProductEntity> products = extentionProductRepository.findAllByShop_Id(extentionShopEntity.getId());
                    products.forEach(productEntity -> {
                        List<ExtensionVariantEntity> variants = extentionVariantRepository.findAllByProduct_Id(productEntity.getId());

                        if (variants.size() > 0) {
                            variants.forEach(extensionVariant -> {
                                Double priceDefault, price1, price2, price3, price4 = Double.valueOf(0);
                                if (productEntity.getPrice1() != null && productEntity.getPrice2() != null && productEntity.getPrice3() != null && productEntity.getPrice1() > productEntity.getPrice2() && productEntity.getPrice1() > productEntity.getPrice3()) {
                                    priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactorDefault().doubleValue();
                                    price1 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor1().doubleValue();
                                    price2 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice2() * extentionShopEntity.getFactor2().doubleValue();
                                    price3 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor3().doubleValue();
                                    price4 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice3() * extentionShopEntity.getFactor4().doubleValue();
                                } else {
                                    priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactorDefault().doubleValue();
                                    price1 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor1().doubleValue();
                                    price2 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor2().doubleValue();
                                    price3 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor3().doubleValue();
                                    price4 = extentionShopEntity.getExchangeRate().doubleValue() * extensionVariant.getPrice() * extentionShopEntity.getFactor4().doubleValue();
                                }
                                extensionVariant.setPriceDefault(round(priceDefault.longValue()));
                                extensionVariant.setPrice1(round(price1.longValue()));
                                extensionVariant.setPrice2(round(price2.longValue()));
                                extensionVariant.setPrice3(round(price3.longValue()));
                                extensionVariant.setPrice4(round(price4.longValue()));
                                extentionVariantRepository.save(extensionVariant);
                                if (extensionVariant.getSku() != null) {
                                    VariantEntity variantEntity = variantRepository.findBySkuVi(extensionVariant.getSku());
                                    if (variantEntity != null) {
                                        variantEntity.setPrice(round(priceDefault.longValue()));
                                        variantEntity.setVip1(round(price1.longValue()));
                                        variantEntity.setVip2(round(price2.longValue()));
                                        variantEntity.setVip3(round(price3.longValue()));
                                        variantEntity.setVip4(round(price4.longValue()));
                                        variantRepository.save(variantEntity);
                                    }
                                }
                            });
                        } else {
                            if (productEntity.getQuantity() != null && productEntity.getSku() != null) {
                                String sku = productEntity.getSku() + "-1";
                                VariantEntity variantEntity = variantRepository.findBySkuVi(sku);
                                if (variantEntity != null) {
                                    Double priceDefault = Double.valueOf(0);
                                    Double price1 = Double.valueOf(0);
                                    Double price2 = Double.valueOf(0);
                                    Double price3 = Double.valueOf(0);
                                    Double price4 = Double.valueOf(0);
                                    if (productEntity.getPrice1() != null) {
                                        priceDefault = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactorDefault().doubleValue();
                                        price1 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor1().doubleValue();
                                        price2 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor2().doubleValue();
                                        price3 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor3().doubleValue();
                                        price4 = extentionShopEntity.getExchangeRate().doubleValue() * productEntity.getPrice1() * extentionShopEntity.getFactor4().doubleValue();
                                    }
                                    variantEntity.setPrice(round(priceDefault.longValue()));
                                    variantEntity.setVip1(round(price1.longValue()));
                                    variantEntity.setVip2(round(price2.longValue()));
                                    variantEntity.setVip3(round(price3.longValue()));
                                    variantEntity.setVip4(round(price4.longValue()));
                                    variantRepository.save(variantEntity);
                                }

                            }
                        }


                    });

                }
                progressService.save(i++, shops.size(), PROGRESS_CODE);
            }
            progressService.delete(PROGRESS_CODE);
        }, new Date());

    }

    @Override
    public void updateEnablePrice(Integer id, Boolean value) {
        ExtensionShopEntity shopEntity = extentionShopRepository.findById(id).orElse(null);
        if (shopEntity != null) {
            shopEntity.setEnableUpdatePrice(value);
            extentionShopRepository.save(shopEntity);
        }
    }


    String generateContent(List<String> gallery) {
        UrlValidator urlValidator = new UrlValidator();
        StringBuilder builder = new StringBuilder("");
        for (String image : gallery) {
            if (image.indexOf("14745115767472")<0){
                builder.append("<figure class=\"image\"><img src=\"" + (urlValidator.isValid(image) ? image : (env.getProperty("spring.domain") + image)) + "\"></figure>");
            }
        }
        return builder.toString();
    }

    private Picture addPicture(String url, Row row, XSSFWorkbook workbook, XSSFSheet sheet) throws Exception {
        /*** image * */
        //Get the contents of an InputStream as a byte[].
        byte[] input = getInputImage(url);
        if (input != null) {
            InputStream imageInput = new ByteArrayInputStream(input);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = IOUtils.toByteArray(imageInput);
            BufferedImage bufferedImage = ImageUtils.cropAndResize(bytes, 200);
            ImageIO.write(bufferedImage, FilenameUtils.getExtension(url), byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();

            //Adds a picture to the workbook
            int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
            //close the input stream
            imageInput.close();
            //Returns an object that handles instantiating concrete classes
            CreationHelper helper = workbook.getCreationHelper();
            //Creates the top-level drawing patriarch.
            Drawing drawing = sheet.createDrawingPatriarch();
            //Create an anchor that is attached to the worksheet
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(new CellAddress("A" + row.getRowNum()).getColumn());
            anchor.setRow1(row.getRowNum()); //Row 3
            anchor.setCol2(new CellAddress("B" + row.getRowNum()).getColumn());
            anchor.setRow2(row.getRowNum() + 1); //Row 4
            Picture pict = drawing.createPicture(anchor, pictureIdx);
//        setHyperlinkToPicture(pict, url);
            Cell cell = row.createCell(new CellAddress("A" + row.getRowNum()).getColumn());
            //set width to n character widths = count characters * 256
            int widthUnits = 40 * 20;
            sheet.setColumnWidth(cell.getColumnIndex(), widthUnits);
            //set height to n points in twips = n * 20
            short heightUnits = 40 * 20;
            cell.getRow().setHeight(heightUnits);
            /*
             *end image
             * */
            byteArrayOutputStream.close();
            return pict;
        }

        return null;
    }

    private void setHyperlinkToPicture(Picture picture, String hyperlinkurl) throws Exception {
        if (picture instanceof XSSFPicture) {
            XSSFPicture xssfpicture = (XSSFPicture) picture;

            XSSFDrawing drawing = xssfpicture.getSheet().createDrawingPatriarch();
            PackageRelationship packagerelationship =
                    drawing.getPackagePart().addExternalRelationship(hyperlinkurl, PackageRelationshipTypes.HYPERLINK_PART);
            String rid = packagerelationship.getId();

            CTPicture ctpicture = xssfpicture.getCTPicture();
            CTPictureNonVisual ctpicturenonvisual = ctpicture.getNvPicPr();
            if (ctpicturenonvisual == null) ctpicturenonvisual = ctpicture.addNewNvPicPr();
            CTNonVisualDrawingProps ctnonvisualdrawingprops = ctpicturenonvisual.getCNvPr();
            if (ctnonvisualdrawingprops == null) ctnonvisualdrawingprops = ctpicturenonvisual.addNewCNvPr();
            CTHyperlink cthyperlink = ctnonvisualdrawingprops.getHlinkClick();
            if (cthyperlink == null) cthyperlink = ctnonvisualdrawingprops.addNewHlinkClick();
            cthyperlink.setId(rid);
        }
    }

    private void downloadVideo(String id) {
        File s = UploadFileUtils.getPath("static/video");
        File file = new File(s.getPath() + "/" + id + ".mp4");
        if (!file.exists()) {
            ResponseEntity<byte[]> video = restTemplate.getForEntity("http://cloud.video.taobao.com/play/u/3944025567/p/2/e/6/t/1/" + id + ".mp4", byte[].class);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                outputStream.write(video.getBody());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpClientErrorException httpClientErrorException) {
                httpClientErrorException.printStackTrace();
            }
        }
    }

    private byte[] getInputImage(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)) {
            try {

                try {
                    ResponseEntity<byte[]> inputImage = restTemplate.getForEntity(url, byte[].class);
                    if (inputImage.getStatusCodeValue() == 200) {
                        return inputImage.getBody();
                    }
                } catch (HttpServerErrorException e) {
                    e.printStackTrace();
                }
            } catch (HttpClientErrorException httpClientErrorException) {
            }
        } else {
            try {
                File s = UploadFileUtils.getPath("static/");
                File folder = new File(s.getPath() + "/upload");
                File image = new File(folder.getPath() + "/" + FilenameUtils.getName(url));
                InputStream inputStream = new FileInputStream(image);
                return inputStream.readAllBytes();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    private void generateHeader(Row header) {
        header.createCell(new CellAddress("A1").getColumn()).setCellValue("Hình ảnh");
        header.createCell(new CellAddress("B1").getColumn()).setCellValue("Phân Loại");
        header.createCell(new CellAddress("C1").getColumn()).setCellValue("* SKU");
        header.createCell(new CellAddress("D1").getColumn()).setCellValue("SKU Biến Thể");
        header.createCell(new CellAddress("E1").getColumn()).setCellValue("SKU Biến Thể Con");
        header.createCell(new CellAddress("F1").getColumn()).setCellValue("Tên TQ");
        header.createCell(new CellAddress("G1").getColumn()).setCellValue("Tên SP UP Web");
        header.createCell(new CellAddress("H1").getColumn()).setCellValue("Tên Biến thể");
        header.createCell(new CellAddress("I1").getColumn()).setCellValue("Dịch Chuẩn biến thể");
        header.createCell(new CellAddress("J1").getColumn()).setCellValue("Tên Biến Thể Con");
        header.createCell(new CellAddress("K1").getColumn()).setCellValue("Dịch Chuẩn biến thể con");
        header.createCell(new CellAddress("L1").getColumn()).setCellValue("Mã Danh Mục");
        header.createCell(new CellAddress("M1").getColumn()).setCellValue("Mã Vạch");
        header.createCell(new CellAddress("N1").getColumn()).setCellValue("Giá TQ 1");
        header.createCell(new CellAddress("O1").getColumn()).setCellValue("Giá TQ 2");
        header.createCell(new CellAddress("P1").getColumn()).setCellValue("Giá TQ 3");
        header.createCell(new CellAddress("Q1").getColumn()).setCellValue("Đơn Vị");
        header.createCell(new CellAddress("R1").getColumn()).setCellValue("Điều Kiên 1");
        header.createCell(new CellAddress("S1").getColumn()).setCellValue("Điều Kiên 2");
        header.createCell(new CellAddress("T1").getColumn()).setCellValue("Điều Kiên 3");
        header.createCell(new CellAddress("U1").getColumn()).setCellValue("Giá Vốn Khởi Tạo");
        header.createCell(new CellAddress("V1").getColumn()).setCellValue("Tỉ Giá");
        header.createCell(new CellAddress("W1").getColumn()).setCellValue("Tồn kho ");
        header.createCell(new CellAddress("X1").getColumn()).setCellValue("Bán Lẻ");
        header.createCell(new CellAddress("Y1").getColumn()).setCellValue("VIP1");
        header.createCell(new CellAddress("Z1").getColumn()).setCellValue("VIP2");
        header.createCell(new CellAddress("AA1").getColumn()).setCellValue("VIP3");
        header.createCell(new CellAddress("AB1").getColumn()).setCellValue("VIP4");
        header.createCell(new CellAddress("AC1").getColumn()).setCellValue("Mô tả sản phẩm");
        header.createCell(new CellAddress("AD1").getColumn()).setCellValue("Trọng lượng");
        header.createCell(new CellAddress("AE1").getColumn()).setCellValue("Kích thước");
        header.createCell(new CellAddress("AF1").getColumn()).setCellValue("Số lượng");
        header.createCell(new CellAddress("AG1").getColumn()).setCellValue("các thông số khác  thu thập đc trong phần phía dưới bài viết (Thông tin thuộc tính) có cản bản gốc và bản dịc");
        header.createCell(new CellAddress("AH1").getColumn()).setCellValue("video");
        header.createCell(new CellAddress("AI1").getColumn()).setCellValue("Ảnh đại diện");
        header.createCell(new CellAddress("AJ1").getColumn()).setCellValue("Ảnh biến thể");
        header.createCell(new CellAddress("AK1").getColumn()).setCellValue("Đơn vị(TQ)");
        header.createCell(new CellAddress("AL1").getColumn()).setCellValue("Mã HTML");
        header.createCell(new CellAddress("AM1").getColumn()).setCellValue("Từ khóa chính");
    }

    private Long round(Long price) {
        Integer placee = 1;
        if (price >= 20000) {
            placee = 4;
        } else if (price <= 1000) {
            placee = 2;
        } else {
            placee = 3;
        }
        Double scale = Math.pow(10, placee);
        Double round = Precision.round((price / scale), 1);
        Double result = round * scale;
        return result.longValue();
    }

    private String translate(String src) {
        if (src != null) {
            DictionaryEntity dictionaryEntity = dictionaryService.findByZhWord(src.trim());
            if (dictionaryEntity != null) {
                return dictionaryEntity.getViWord();
            }
        }

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/translate?api-version=3.0&to=vi&textType=plain&profanityAction=NoAction"))
//                .header("content-type", "application/json")
//                .header("x-rapidapi-key", "22799d8ad8msh172c5ca87d072b8p17e5c1jsne6e33bf0b807")
//                .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
//                .method("POST", HttpRequest.BodyPublishers.ofString("[{\"Text\": \""+src+"\"}]"))
//                .build();
//        HttpResponse<String> response = null;
//        try {
//            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//            List<Map> maps = (List<Map>) SpringUtils.convertJsonToObject(response.body(),List.class);
//            if (maps!=null && maps.size()>0){
//                List<Map> translations = (List) maps.get(0).get("translations");
//                if (translations!=null){
//                    return String.valueOf(translations.get(0).get("text"));
//                }
//            }
//        } catch (IOException e) {
//
//        } catch (InterruptedException e) {
//
//        }

        return src;
    }

}
