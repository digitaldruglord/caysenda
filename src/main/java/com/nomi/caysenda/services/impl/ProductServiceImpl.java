package com.nomi.caysenda.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomi.caysenda.api.admin.model.product.request.AdminProductRequest;
import com.nomi.caysenda.api.admin.model.product.request.AdminProductVariantRequest;
import com.nomi.caysenda.api.admin.model.product.response.AdminProductDTO;
import com.nomi.caysenda.dialect.utils.ProductUtilsDialect;
import com.nomi.caysenda.dto.ProductAndVariantDTO;
import com.nomi.caysenda.dto.ProductDTO;
import com.nomi.caysenda.dto.ProductGallery;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.exceptions.product.ProductException;
import com.nomi.caysenda.lazada.services.LazadaProductService;
import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.services.CartService;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.services.SettingService;
import com.nomi.caysenda.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired ProductRepository productRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired VariantRepository variantRepository;
    @Autowired private Environment env;
    @Autowired ProviderRepository providerRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired RestTemplate restTemplate;
    @Autowired TaskScheduler taskScheduler;
    @Autowired TemplateEngine templateEngine;
    @Autowired SettingService settingService;
    @Autowired CategoryService categoryService;
    @Autowired CartService cartService;
    @Autowired LazadaProductService lazadaProductService;

    @Override
    public List<ProductEntity> findAllByIds(List<Integer> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public ProductEntity findBySlugVi(String slug) {
        return productRepository.findBySlugVi(slug);
    }

    @Override
    public ProductEntity findBySkuVi(String sku) {
        return productRepository.findBySkuVi(sku);
    }

    @Override
    public void delete(Integer id) {
        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        productRepository.deleteById(id);
        if (productEntity!=null){
            try {
                lazadaProductService.removeByProductSku(productEntity.getSkuVi());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        List<ProductEntity> products = productRepository.findAllById(ids);
        productRepository.deleteAll(products);
    }


    @Override
    public ProductEntity findById(Integer id) {
        ProductEntity optional = productRepository.findById(id).orElse(null);
        if (optional!= null) {
            List<CategoryEntity> categories = categoryRepository.findAllByProducts(optional);
            optional.setCategories(categories);
            return optional;
        }
        return null;
    }

    @Override
    public Page findAll(Pageable pageable) {
        Page<AdminProductDTO> products = productRepository.customFindAllForAdminProductDTO(pageable);
        products.forEach(adminProductDTO -> {
            adminProductDTO.setSlug(getFullURL(adminProductDTO.getSlug(), adminProductDTO.getCategorySlug()));
        });
        return products;
    }

    @Override
    public Page findAll(String keyword, Pageable pageable) {
        Page<AdminProductDTO> products = productRepository.customFindAllForAdminProductDTO(keyword, pageable);
        products.forEach(adminProductDTO -> {
            adminProductDTO.setSlug(getFullURL(adminProductDTO.getSlug(), adminProductDTO.getCategorySlug()));
        });
        return products;
    }

    @Override
    public Page findAll(String keyword, Integer catId, Pageable pageable) {
        Page<AdminProductDTO> products = productRepository.customFindAllForAdminProductDTO(keyword, catId, pageable);
        products.forEach(adminProductDTO -> {
            adminProductDTO.setSlug(getFullURL(adminProductDTO.getSlug(), adminProductDTO.getCategorySlug()));
        });
        return products;
    }

    @Override
    public Page findByKeyword(String keyword) {
        return null;
    }

    @Override
    public Page findByKeywordAndCat(String keyword) {
        return null;
    }

    @Override
    public ProductEntity create(AdminProductRequest productRequest) throws ProductException {
        if (productRepository.existsBySkuVi(productRequest.getSkuVi()))
            throw new ProductException(ProductException.EXISTS_SKUVI);
        if (productRepository.existsBySkuZh(productRequest.getSkuZh()))
            throw new ProductException(ProductException.EXISTS_SKUZH);
        if (productRepository.existsBySlugVi(ConvertStringToUrl.covertStringToURL(productRequest.getNameVi())))
            throw new ProductException(ProductException.EXISTS_SLUG);
        /**  Product */
        ProductEntity productEntity = new ProductEntity();
        productEntity.setNameZh(productRequest.getNameZh());
        productEntity.setSkuZh(productRequest.getSkuZh());
        productEntity.setNameVi(productRequest.getNameVi());
        productEntity.setSkuVi(productRequest.getSkuVi());
        productEntity.setSlugVi(productRequest.getNameVi() != null ? ConvertStringToUrl.covertStringToURL(productRequest.getNameVi()) : RandomStringUtils.randomAlphabetic(10));
        productEntity.setThumbnail(productRequest.getThumbnail());
        productEntity.setStatus(1);
        productEntity.setContent(productRequest.getContent());

        productEntity.setTrash(false);
        if (productRequest.getRetail()!=null && productRequest.getRetail()){
            productEntity.setConditiondefault(productRequest.getConditiondefault());
            productEntity.setCondition1(productRequest.getCondition1());
            productEntity.setCondition2(productRequest.getCondition2());
            productEntity.setCondition3(productRequest.getCondition3());
            productEntity.setCondition4(productRequest.getCondition4());
        }else {
            productEntity.setConditiondefault(productRequest.getConditiondefault());
            productEntity.setCondition1(productRequest.getCondition1());
            productEntity.setCondition2(productRequest.getCondition2());
            productEntity.setCondition3(productRequest.getCondition3());
            productEntity.setCondition4(productRequest.getCondition4());
        }

        productEntity.setCategoryDefault(productRequest.getCategoryDefault());
        productEntity.setGallery(productRequest.getGallery());
        productEntity.setLink(productRequest.getLink());
        productEntity.setDescription(productRequest.getDescription());
        productEntity.setMaterial(productRequest.getMaterial());
        productEntity.setAverageWeight(productRequest.getAverageWeight());
        productEntity.setUnit(productRequest.getUnit());
        productEntity.setSold(Long.valueOf(new Random().nextInt(1000)));
        productEntity.setVideo(productRequest.getVideo());
        productEntity.setKeyword(productRequest.getKeyword());
        productEntity.setTopFlag("0");
        if (productRequest.getEnableAutoUpdatePrice() != null) {
            productEntity.setEnableAutoUpdatePrice(productRequest.getEnableAutoUpdatePrice());
        } else {
            productEntity.setEnableAutoUpdatePrice(true);
        }
        /**  Category  */
        List<CategoryEntity> categories = categoryRepository.findAllById(productRequest.getCategories());
        CategoryEntity categoryDefault = categories.stream().filter((categoryEntity -> categoryEntity.getId().equals(productRequest.getCategoryDefault()))).findAny().orElse(null);
        productEntity.setCategories(categories);
        /** provider */
        List<ProviderEntity> providerEntities = providerRepository.findAllById(productRequest.getProviders());
        productEntity.setProviders(providerEntities);
        try {
            productEntity = productRepository.save(productEntity);
        }catch ( DataIntegrityViolationException e){
            throw new ProductException(ProductException.DATA_EXCEPTION);
        }

        /**  Variant */
        List<VariantEntity> variants = new ArrayList<>();
        List<GroupVariantEntity> groups = productRequest.getVariantGroup()!=null?productRequest.getVariantGroup():new ArrayList<>();
        if (productRequest.getVariants() != null && productRequest.getVariants().size() > 0) {
            for (AdminProductVariantRequest variantRequest : productRequest.getVariants()) {
                VariantEntity variantEntity = new VariantEntity();
                variantEntity.setProductEntity(productEntity);
                variantEntity.setNameZh(variantRequest.getNameZh());
                variantEntity.setSkuZh(variantRequest.getSkuZh());
                variantEntity.setNameVi(variantRequest.getNameVi());
                variantEntity.setSkuVi(variantRequest.getSkuVi());
                variantEntity.setThumbnail(variantRequest.getThumbnail());
                variantEntity.setWeight(variantRequest.getWeight());
                variantEntity.setWidth(variantRequest.getWidth());
                variantEntity.setHeight(variantRequest.getHeight());
                variantEntity.setLength(variantRequest.getLength());
                variantEntity.setStock(variantRequest.getStock());
                variantEntity.setPriceZh(variantRequest.getPriceZh());
                variantEntity.setDimension(variantRequest.getDimension());
                variantEntity.setCost(variantRequest.getCost());
                if (variantRequest.getParent() != null) {
                    GroupVariantEntity isExises = groups.stream().filter(groupVariantEntity -> groupVariantEntity.getZhName().equals(variantRequest.getParent())).findAny().orElse(null);
                    if (isExises != null) {
                        variantEntity.setParent(isExises.getSkuGroup());
                        for (GroupVariantEntity groupVariantEntity:groups){
                            if (groupVariantEntity.getSkuGroup().equals(isExises.getSkuGroup())){
                                groupVariantEntity.setName(variantRequest.getParentTemp()!=null?variantRequest.getParentTemp():translate(variantRequest.getParent()));
                            }
                        }
                    } else {
                        GroupVariantEntity groupVariantEntity = new GroupVariantEntity();
                        groupVariantEntity.setSkuGroup(variantEntity.getSkuVi() + "PARENT");
                        groupVariantEntity.setName(variantRequest.getParentTemp()!=null?variantRequest.getParentTemp():translate(variantRequest.getParent()));
                        groupVariantEntity.setZhName(variantRequest.getParent());
                        groupVariantEntity.setThumbnail(variantRequest.getThumbnail());
                        groups.add(groupVariantEntity);
                        variantEntity.setParent(groupVariantEntity.getSkuGroup());
                    }
                }
                if (productRequest.getEnableAutoUpdatePrice() != null && productRequest.getEnableAutoUpdatePrice() && categoryDefault != null) {
                    variantEntity.setPrice(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactorDefault().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip1(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor1().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip2(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor2().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip3(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor3().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip4(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor4().longValue() * variantRequest.getPriceZh().longValue()));

                } else {
                    variantEntity.setPrice(variantRequest.getPrice());
                    variantEntity.setVip1(variantRequest.getVip1());
                    variantEntity.setVip2(variantRequest.getVip2());
                    variantEntity.setVip3(variantRequest.getVip3());
                    variantEntity.setVip4(variantRequest.getVip4());
                }
                variants.add(variantEntity);
            }
        }
        productEntity.setVariants(variants);
        productEntity.setVariantGroup(groups);
        productEntity.setQuickviewGallery(productRequest.getGallery().stream().limit(4).collect(Collectors.toList()));
        productEntity = productRepository.save(productEntity);
        return productEntity;

    }

    @Override
    public ProductEntity update(AdminProductRequest productRequest) {
        /**  Product */
        ProductEntity productEntity = productRepository.findById(productRequest.getId()).get();
        if (productRequest.getNameZh() != null) productEntity.setNameZh(productRequest.getNameZh());
        if (productRequest.getSkuZh() != null) productEntity.setSkuZh(productRequest.getSkuZh());
        if (productRequest.getNameVi() != null) productEntity.setNameVi(productRequest.getNameVi());
        if (productRequest.getSkuVi() != null) productEntity.setSkuVi(productRequest.getSkuVi());
        if (productRequest.getNameVi() != null) productEntity.setSlugVi(ConvertStringToUrl.covertStringToURL(productRequest.getNameVi()));
        if (productRequest.getThumbnail() != null) productEntity.setThumbnail(productRequest.getThumbnail());
        if (productRequest.getStatus() != null) productEntity.setStatus(productRequest.getStatus());
        if (productRequest.getContent() != null) productEntity.setContent(productRequest.getContent());
        if (productRequest.getConditiondefault() != null) productEntity.setConditiondefault(productRequest.getConditiondefault());
        if (productRequest.getCondition1() != null) productEntity.setCondition1(productRequest.getCondition1());
        if (productRequest.getCondition2() != null) productEntity.setCondition2(productRequest.getCondition2());
        if (productRequest.getCondition3() != null) productEntity.setCondition3(productRequest.getCondition3());
        if (productRequest.getCondition4() != null) productEntity.setCondition4(productRequest.getCondition4());
        productEntity.setVideo(productRequest.getVideo());
        if (productRequest.getCategoryDefault() != null) productEntity.setCategoryDefault(productRequest.getCategoryDefault());
        if (productRequest.getGallery() != null) productEntity.setGallery(productRequest.getGallery());
        if (productRequest.getGallery() != null) productEntity.setQuickviewGallery(productRequest.getGallery().stream().limit(4).collect(Collectors.toList()));
        if (productRequest.getEnableAutoUpdatePrice() != null) productEntity.setEnableAutoUpdatePrice(productRequest.getEnableAutoUpdatePrice());
        if (productRequest.getLink() != null) productEntity.setLink(productRequest.getLink());
        if (productRequest.getMaterial() != null) productEntity.setMaterial(productRequest.getMaterial());
        if (productRequest.getDescription() != null) productEntity.setDescription(productRequest.getDescription());
        if (productRequest.getAverageWeight() != null) productEntity.setAverageWeight(productRequest.getAverageWeight());
        if (productRequest.getUnit() != null) productEntity.setUnit(productRequest.getUnit());
        if (productRequest.getKeyword() != null) productEntity.setKeyword(productRequest.getKeyword());
        if (productEntity.getSold()==null || productEntity.getSold().equals(0)){
            productEntity.setSold(Long.valueOf(new Random().nextInt(1000)));
        }
        if (productRequest.getTopFlag() !=null && !productRequest.getTopFlag().equals("")) productEntity.setTopFlag(productRequest.getTopFlag());
        /**  Category  */
        List<CategoryEntity> categories = categoryRepository.findAllById(productRequest.getCategories());
        CategoryEntity categoryDefault = categories.stream().filter((categoryEntity -> categoryEntity.getId().equals(productEntity.getCategoryDefault()))).findAny().orElse(null);
        if (categories != null) productEntity.setCategories(categories);

        /** Delete variant */
        for (VariantEntity variantEntity : productEntity.getVariants()) {
            AdminProductVariantRequest variantRequest = productRequest.getVariants().stream().filter((variant) -> variantEntity.getId().equals(variant.getId())).findAny().orElse(null);
            if (variantRequest == null) {
                variantRepository.delete(variantEntity);
            }
        }

        /**  Variant */
        List<VariantEntity> variants = new ArrayList<>();
        List<GroupVariantEntity> groups = productRequest.getVariantGroup()!=null?productRequest.getVariantGroup():new ArrayList<>();
        if (productRequest.getVariants() != null && productRequest.getVariants().size() > 0) {
            for (AdminProductVariantRequest variantRequest : productRequest.getVariants()) {
                VariantEntity variantEntity = new VariantEntity();
                if (variantRequest.getId() != null) {
                    variantEntity = variantRepository.findById(variantRequest.getId()).get();
                } else {
                    variantEntity.setProductEntity(productEntity);
                }
                if (variantRequest.getNameZh() != null) {
                    variantEntity.setNameZh(variantRequest.getNameZh());
                }
                if (variantRequest.getSkuZh() != null) variantEntity.setSkuZh(variantRequest.getSkuZh());
                if (variantRequest.getNameVi() != null) variantEntity.setNameVi(variantRequest.getNameVi());
                if (variantRequest.getSkuVi() != null) variantEntity.setSkuVi(variantRequest.getSkuVi());
                if (variantRequest.getThumbnail() != null) variantEntity.setThumbnail(variantRequest.getThumbnail());
                if (variantRequest.getWeight() != null) variantEntity.setWeight(variantRequest.getWeight());
                if (variantRequest.getWidth() != null) variantEntity.setWidth(variantRequest.getWidth());
                if (variantRequest.getHeight() != null) variantEntity.setHeight(variantRequest.getHeight());
                if (variantRequest.getLength() != null) variantEntity.setLength(variantRequest.getLength());
                if (variantRequest.getStock() != null) variantEntity.setStock(variantRequest.getStock());
                if (variantRequest.getPriceZh() != null) variantEntity.setPriceZh(variantRequest.getPriceZh());
                if (variantRequest.getDimension() != null) variantEntity.setDimension(variantRequest.getDimension());
                if (variantRequest.getCost()!=null) variantEntity.setCost(variantRequest.getCost());
                if (productRequest.getEnableAutoUpdatePrice() && categoryDefault != null) {
                    variantEntity.setPrice(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactorDefault().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip1(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor1().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip2(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor2().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip3(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor3().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip4(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor4().longValue() * variantRequest.getPriceZh().longValue()));

                } else {
                    if (variantRequest.getPrice() != null) variantEntity.setPrice(variantRequest.getPrice());
                    if (variantRequest.getVip1() != null) variantEntity.setVip1(variantRequest.getVip1());
                    if (variantRequest.getVip2() != null) variantEntity.setVip2(variantRequest.getVip2());
                    if (variantRequest.getVip3() != null) variantEntity.setVip3(variantRequest.getVip3());
                    if (variantRequest.getVip4() != null) variantEntity.setVip4(variantRequest.getVip4());
                }
                if (variantRequest.getParent() != null) {
                    GroupVariantEntity isExises = groups.stream().filter(groupVariantEntity -> groupVariantEntity.getZhName().equals(variantRequest.getParent())).findAny().orElse(null);
                    if (isExises != null) {
                        variantEntity.setParent(isExises.getSkuGroup());
                        groups.forEach(groupVariantEntity -> {
                            if (groupVariantEntity.getSkuGroup().equals(isExises.getSkuGroup())){
                                groupVariantEntity.setName(variantRequest.getParentTemp()!=null?variantRequest.getParentTemp():translate(variantRequest.getParent()));
                            }
                        });
                    } else {
                        GroupVariantEntity groupVariantEntity = new GroupVariantEntity();
                        groupVariantEntity.setSkuGroup(variantEntity.getSkuVi() + "PARENT");
                        groupVariantEntity.setName(variantRequest.getParentTemp()!=null?variantRequest.getParentTemp():translate(variantRequest.getParent()));
                        groupVariantEntity.setZhName(variantRequest.getParent());
                        groupVariantEntity.setThumbnail(variantRequest.getThumbnail());
                        groups.add(groupVariantEntity);
                        variantEntity.setParent(groupVariantEntity.getSkuGroup());

                    }
                }

                variants.add(variantEntity);
            }
        }
        productEntity.setVariantGroup(groups);
        productEntity.setVariants(variantRepository.saveAll(variants));
        return productRepository.save(productEntity);
    }

    @Override
    public void update(ProductEntity productEntity) {
        productRepository.save(productEntity);
    }

    @Override
    public void updateTopFlag(Integer id, String flag) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity.isPresent()) {
            ProductEntity product = productEntity.get();
            product.setTopFlag(flag);
            productRepository.save(product);
        }
    }

    @Override
    public ProductEntity createAdmin(AdminProductRequest productRequest) throws ProductException {
        if (productRepository.existsBySkuVi(productRequest.getSkuVi()))
            throw new ProductException(ProductException.EXISTS_SKUVI);
        if (productRepository.existsBySkuZh(productRequest.getSkuZh()))
            throw new ProductException(ProductException.EXISTS_SKUZH);
        if (productRepository.existsBySlugVi(ConvertStringToUrl.covertStringToURL(productRequest.getNameVi())))
            throw new ProductException(ProductException.EXISTS_SLUG);
        /**  Product */
        ProductEntity productEntity = new ProductEntity();
        productEntity.setNameZh(productRequest.getNameZh());
        productEntity.setSkuZh(productRequest.getSkuZh());
        productEntity.setNameVi(productRequest.getNameVi());
        productEntity.setSkuVi(productRequest.getSkuVi());
        productEntity.setSlugVi(productRequest.getNameVi() != null ? ConvertStringToUrl.covertStringToURL(productRequest.getNameVi()) : RandomStringUtils.randomAlphabetic(10));
        productEntity.setThumbnail(productRequest.getThumbnail());
        productEntity.setStatus(1);
        productEntity.setContent(productRequest.getContent());
        productEntity.setTrash(false);
        if (productRequest.getRetail()!=null && productRequest.getRetail()){
            productEntity.setConditiondefault(1);
            productEntity.setCondition1(productRequest.getCondition1());
            productEntity.setCondition2(productRequest.getCondition2());
            productEntity.setCondition3(productRequest.getCondition3());
            productEntity.setCondition4(productRequest.getCondition4());
        }else {
            productEntity.setConditiondefault(productRequest.getConditiondefault());
            productEntity.setCondition1(productRequest.getCondition1());
            productEntity.setCondition2(productRequest.getCondition2());
            productEntity.setCondition3(productRequest.getCondition3());
            productEntity.setCondition4(productRequest.getCondition4());
        }

        productEntity.setCategoryDefault(productRequest.getCategoryDefault());
        productEntity.setGallery(productRequest.getGallery());
        productEntity.setLink(productRequest.getLink());
        productEntity.setDescription(productRequest.getDescription());
        productEntity.setMaterial(productRequest.getMaterial());
        productEntity.setAverageWeight(productRequest.getAverageWeight());
        productEntity.setUnit(productRequest.getUnit());
        productEntity.setSold(Long.valueOf(new Random().nextInt(1000)));
        productEntity.setKeyword(productRequest.getKeyword());
        if (productRequest.getEnableAutoUpdatePrice() != null) {
            productEntity.setEnableAutoUpdatePrice(productRequest.getEnableAutoUpdatePrice());
        } else {
            productEntity.setEnableAutoUpdatePrice(true);
        }
        /**  Category  */
        List<CategoryEntity> categories = categoryRepository.findAllById(productRequest.getCategories());
        CategoryEntity categoryDefault = categories.stream().filter((categoryEntity -> categoryEntity.getId().equals(productRequest.getCategoryDefault()))).findAny().orElse(null);
        productEntity.setCategories(categories);
        /** provider */
        List<ProviderEntity> providerEntities = providerRepository.findAllById(productRequest.getProviders());
        productEntity.setProviders(providerEntities);
        productEntity = productRepository.save(productEntity);
        /**  Variant */
        List<VariantEntity> variants = new ArrayList<>();
        List<GroupVariantEntity> groups = productRequest.getVariantGroup()!=null?productRequest.getVariantGroup():new ArrayList<>();
        if (productRequest.getVariants() != null && productRequest.getVariants().size() > 0) {
            for (AdminProductVariantRequest variantRequest : productRequest.getVariants()) {
                VariantEntity variantEntity = new VariantEntity();
                variantEntity.setProductEntity(productEntity);
                variantEntity.setNameZh(variantRequest.getNameZh());
                variantEntity.setSkuZh(variantRequest.getSkuZh());
                variantEntity.setNameVi(variantRequest.getNameVi());
                variantEntity.setSkuVi(variantRequest.getSkuVi());
                variantEntity.setThumbnail(variantRequest.getThumbnail());
                variantEntity.setWeight(variantRequest.getWeight());
                variantEntity.setWidth(variantRequest.getWidth());
                variantEntity.setHeight(variantRequest.getHeight());
                variantEntity.setLength(variantRequest.getLength());
                variantEntity.setStock(variantRequest.getStock());
                variantEntity.setPriceZh(variantRequest.getPriceZh());
                variantEntity.setDimension(variantRequest.getDimension());
                variantEntity.setCost(variantRequest.getCost());
                if (variantRequest.getParent() != null) {
                   variantEntity.setParent(variantRequest.getParent());
                }
                if (productRequest.getEnableAutoUpdatePrice() != null && productRequest.getEnableAutoUpdatePrice() && categoryDefault != null) {
                    variantEntity.setPrice(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactorDefault().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip1(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor1().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip2(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor2().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip3(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor3().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip4(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor4().longValue() * variantRequest.getPriceZh().longValue()));

                } else {
                    variantEntity.setPrice(variantRequest.getPrice());
                    variantEntity.setVip1(variantRequest.getVip1());
                    variantEntity.setVip2(variantRequest.getVip2());
                    variantEntity.setVip3(variantRequest.getVip3());
                    variantEntity.setVip4(variantRequest.getVip4());
                }
                variants.add(variantEntity);
            }
        }
        productEntity.setVariants(variants);
        productEntity.setVariantGroup(groups);
        productEntity.setQuickviewGallery(productRequest.getGallery().stream().limit(4).collect(Collectors.toList()));
        productEntity = productRepository.save(productEntity);
        return productEntity;
    }

    @Override
    public ProductEntity updateAdmin(AdminProductRequest productRequest) {
        /**  Product */
        ProductEntity productEntity = productRepository.findById(productRequest.getId()).get();
        if (productRequest.getNameZh() != null) productEntity.setNameZh(productRequest.getNameZh());
        if (productRequest.getSkuZh() != null) productEntity.setSkuZh(productRequest.getSkuZh());
        if (productRequest.getNameVi() != null) productEntity.setNameVi(productRequest.getNameVi());
        if (productRequest.getSkuVi() != null) productEntity.setSkuVi(productRequest.getSkuVi());
        if (productRequest.getNameVi() != null) productEntity.setSlugVi(ConvertStringToUrl.covertStringToURL(productRequest.getNameVi()));
        if (productRequest.getThumbnail() != null) productEntity.setThumbnail(productRequest.getThumbnail());
        if (productRequest.getStatus() != null) productEntity.setStatus(productRequest.getStatus());
        if (productRequest.getContent() != null) productEntity.setContent(productRequest.getContent());
        if (productRequest.getConditiondefault() != null) productEntity.setConditiondefault(productRequest.getConditiondefault());
        if (productRequest.getCondition1() != null) productEntity.setCondition1(productRequest.getCondition1());
        if (productRequest.getCondition2() != null) productEntity.setCondition2(productRequest.getCondition2());
        if (productRequest.getCondition3() != null) productEntity.setCondition3(productRequest.getCondition3());
        if (productRequest.getCondition4() != null) productEntity.setCondition4(productRequest.getCondition4());
        if (productRequest.getCategoryDefault() != null)
            productEntity.setCategoryDefault(productRequest.getCategoryDefault());
        if (productRequest.getGallery() != null) productEntity.setGallery(productRequest.getGallery());
        if (productRequest.getGallery() != null) productEntity.setQuickviewGallery(productRequest.getGallery().stream().limit(4).collect(Collectors.toList()));
        if (productRequest.getEnableAutoUpdatePrice() != null)
            productEntity.setEnableAutoUpdatePrice(productRequest.getEnableAutoUpdatePrice());
        if (productRequest.getLink() != null) productEntity.setLink(productRequest.getLink());
        if (productRequest.getMaterial() != null) productEntity.setMaterial(productRequest.getMaterial());
        if (productRequest.getDescription() != null) productEntity.setDescription(productRequest.getDescription());
        if (productRequest.getAverageWeight() != null)
            productEntity.setAverageWeight(productRequest.getAverageWeight());
        if (productRequest.getUnit() != null) productEntity.setUnit(productRequest.getUnit());
        if (productRequest.getKeyword() != null) productEntity.setKeyword(productRequest.getKeyword());
        if (productEntity.getSold()==null || productEntity.getSold().equals(0)){
            productEntity.setSold(Long.valueOf(new Random().nextInt(1000)));
        }
        if (productRequest.getTopFlag()!=null && !productRequest.getTopFlag().equals("")) productEntity.setTopFlag(productRequest.getTopFlag());
        /**  Category  */
        List<CategoryEntity> categories = categoryRepository.findAllById(productRequest.getCategories());
        CategoryEntity categoryDefault = categories.stream().filter((categoryEntity -> categoryEntity.getId().equals(productEntity.getCategoryDefault()))).findAny().orElse(null);

        if (categories != null) productEntity.setCategories(categories);
        /** Delete variant */
        for (VariantEntity variantEntity : productEntity.getVariants()) {
            AdminProductVariantRequest variantRequest = productRequest.getVariants().stream().filter((variant) -> variantEntity.getId().equals(variant.getId())).findAny().orElse(null);
            if (variantRequest == null) {
                variantRepository.delete(variantEntity);
            }
        }

        /**  Variant */
        List<VariantEntity> variants = new ArrayList<>();
        List<GroupVariantEntity> groups = productRequest.getVariantGroup()!=null?productRequest.getVariantGroup():new ArrayList<>();
        if (productRequest.getVariants() != null && productRequest.getVariants().size() > 0) {
            for (AdminProductVariantRequest variantRequest : productRequest.getVariants()) {
                VariantEntity variantEntity = new VariantEntity();
                if (variantRequest.getId() != null) {
                    variantEntity = variantRepository.findById(variantRequest.getId()).get();
                } else {
                    variantEntity.setProductEntity(productEntity);
                }
                if (variantRequest.getNameZh() != null) {
                    variantEntity.setNameZh(variantRequest.getNameZh());
                }
                if (variantRequest.getSkuZh() != null) variantEntity.setSkuZh(variantRequest.getSkuZh());
                if (variantRequest.getNameVi() != null) variantEntity.setNameVi(variantRequest.getNameVi());
                if (variantRequest.getSkuVi() != null) variantEntity.setSkuVi(variantRequest.getSkuVi());
                if (variantRequest.getThumbnail() != null) variantEntity.setThumbnail(variantRequest.getThumbnail());
                if (variantRequest.getWeight() != null) variantEntity.setWeight(variantRequest.getWeight());
                if (variantRequest.getWidth() != null) variantEntity.setWidth(variantRequest.getWidth());
                if (variantRequest.getHeight() != null) variantEntity.setHeight(variantRequest.getHeight());
                if (variantRequest.getLength() != null) variantEntity.setLength(variantRequest.getLength());
                if (variantRequest.getStock() != null) variantEntity.setStock(variantRequest.getStock());
                if (variantRequest.getPriceZh() != null) variantEntity.setPriceZh(variantRequest.getPriceZh());
                if (variantRequest.getDimension() != null) variantEntity.setDimension(variantRequest.getDimension());
                if (variantRequest.getCost()!=null) variantEntity.setCost(variantRequest.getCost());
                if (productRequest.getEnableAutoUpdatePrice() && categoryDefault != null) {
                    variantEntity.setPrice(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactorDefault().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip1(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor1().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip2(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor2().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip3(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor3().longValue() * variantRequest.getPriceZh().longValue()));
                    variantEntity.setVip4(NumberUtils.round(categoryDefault.getRate().longValue() * categoryDefault.getFactor4().longValue() * variantRequest.getPriceZh().longValue()));

                } else {
                    if (variantRequest.getPrice() != null) variantEntity.setPrice(variantRequest.getPrice());
                    if (variantRequest.getVip1() != null) variantEntity.setVip1(variantRequest.getVip1());
                    if (variantRequest.getVip2() != null) variantEntity.setVip2(variantRequest.getVip2());
                    if (variantRequest.getVip3() != null) variantEntity.setVip3(variantRequest.getVip3());
                    if (variantRequest.getVip4() != null) variantEntity.setVip4(variantRequest.getVip4());
                }
                if (variantRequest.getParent() != null) {
                    variantEntity.setParent(variantRequest.getParent());
                }
                variants.add(variantEntity);
            }
        }
        productEntity.setVariantGroup(groups);
        productEntity.setVariants(variants);
        return productRepository.save(productEntity);
    }

    @Override
    public Page findAllForProductDTO(Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findAllForProductDTO(host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findAllForProductDTO(String keyword, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findAllForProductDTO("%"+keyword+"%", host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {

            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findAllForProductDTO(String keyword, String catSlug, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findAllForProductDTO("%"+keyword+"%", catSlug, host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findRandForProductDTO(Pageable pageable) {
        String host = env.getProperty("spring.domain");

        Page<ProductDTO> productDTOS = productRepository.findRandForProductDTO(host!=null?host:"", pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public void reduceStock(Integer variantId, Integer quantity) {
        VariantEntity variantEntity = variantRepository.findById(variantId).orElse(null);
        if (variantEntity != null) {
            Integer newStock = variantEntity.getStock() - quantity;
            if (newStock < 0) newStock = 0;
            variantEntity.setStock(newStock);
            variantRepository.save(variantEntity);
        }
    }

    @Override
    public void increaseStock(Integer variantId, Integer quantity) {
        VariantEntity variantEntity = variantRepository.findById(variantId).orElse(null);
        System.out.println(variantEntity.getProductEntity().getId());
        if (variantEntity != null) {
            Integer newStock = variantEntity.getStock() + quantity;
            variantEntity.setStock(newStock);
            variantRepository.save(variantEntity);
        }
    }


    @Override
    public Page<ProductGallery> findRandForProductGallery(Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductGallery> productDTOS = productRepository.findRandForProductGallery(host, pageable);
        for (ProductGallery productGallery : productDTOS.getContent()) {
            productGallery.setSlug(getFullURL(productGallery.getSlug(), productGallery.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findByCategoryRandForProductDTO(Integer categoryId, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findByCategoryRandForProductDTO(categoryId, host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findByCategoryForProductDTO(Integer categoryId, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findByCategoryForProductDTO(categoryId, host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findByCategorySlugForProductDTO(String slug, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findByCategorySlugForProductDTO(slug, host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductDTO> findByHotProductForProductDTO(Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.findByHotProductForProductDTO(host, pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public Page<ProductAndVariantDTO> findAllForProductAndVariantDTOByKeyword(String keyword, Pageable pageable) {
        return variantRepository.findAllForProductAndVariantDTOByKeyword(keyword, pageable);
    }

    @Override
    public Page<ProductAndVariantDTO> findAllForProductAndVariantDTO(Pageable pageable) {
        return variantRepository.findAllForProductAndVariantDTO(pageable);
    }

    @Override
    public void importExcel(MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
        Iterator<String> listName = multipartHttpServletRequest.getFileNames();
        while (listName.hasNext()) {
            MultipartFile multipartFile = multipartHttpServletRequest.getFile(listName.next());
            XSSFWorkbook book = new XSSFWorkbook(multipartFile.getInputStream());
            FormulaEvaluator evaluator = book.getCreationHelper().createFormulaEvaluator();

            XSSFSheet sheet = book.getSheetAt(0);
            int rows = sheet.getLastRowNum();
            int i = 1;
            while (i <= rows) {
                Row row = sheet.getRow(i++);
                /** get data from excel*/
                String productSku = row.getCell(new CellAddress("C" + i).getColumn()) != null ? row.getCell(new CellAddress("C" + i).getColumn()).getStringCellValue() : null;
                String variantSku = row.getCell(new CellAddress("D" + i).getColumn()) != null ? row.getCell(new CellAddress("D" + i).getColumn()).getStringCellValue() : null;
                System.out.println(variantSku);
                String variantChildSku = row.getCell(new CellAddress("E" + i).getColumn()) != null ? row.getCell(new CellAddress("E" + i).getColumn()).getStringCellValue() : null;
                String nameZh = row.getCell(new CellAddress("F" + i).getColumn()) != null ? row.getCell(new CellAddress("F" + i).getColumn()).getStringCellValue() : null;
                String name = row.getCell(new CellAddress("G" + i).getColumn()) != null ? row.getCell(new CellAddress("G" + i).getColumn()).getStringCellValue() : null;
                String variantNameZh = row.getCell(new CellAddress("H" + i).getColumn()) != null ? row.getCell(new CellAddress("H" + i).getColumn()).getStringCellValue() : null;
                String variantName = row.getCell(new CellAddress("I" + i).getColumn()) != null ? row.getCell(new CellAddress("I" + i).getColumn()).getStringCellValue() : null;
                String variantChildNameZh = row.getCell(new CellAddress("J" + i).getColumn()) != null ? row.getCell(new CellAddress("J" + i).getColumn()).getStringCellValue() : null;
                String variantChildName = row.getCell(new CellAddress("K" + i).getColumn()) != null ? row.getCell(new CellAddress("K" + i).getColumn()).getStringCellValue() : null;
                String categorySku = row.getCell(new CellAddress("L" + i).getColumn()) != null ? row.getCell(new CellAddress("L" + i).getColumn()).getStringCellValue() : null;
                String barcode = row.getCell(new CellAddress("M" + i).getColumn()) != null ? row.getCell(new CellAddress("M" + i).getColumn()).getStringCellValue() : null;
                String unit = row.getCell(new CellAddress("Q" + i).getColumn()) != null ? row.getCell(new CellAddress("Q" + i).getColumn()).getStringCellValue() : null;
                Double condition1 = row.getCell(new CellAddress("R" + i).getColumn()) != null ? row.getCell(new CellAddress("R" + i).getColumn()).getNumericCellValue() : 1;
                Double condition2 = row.getCell(new CellAddress("S" + i).getColumn()) != null ? row.getCell(new CellAddress("S" + i).getColumn()).getNumericCellValue() : null;
                Double condition3 = row.getCell(new CellAddress("T" + i).getColumn()) != null ? row.getCell(new CellAddress("T" + i).getColumn()).getNumericCellValue() : null;
                Double cost = row.getCell(new CellAddress("U" + i).getColumn()) != null ? row.getCell(new CellAddress("U" + i).getColumn()).getNumericCellValue() : 0;
                Double stock = row.getCell(new CellAddress("W" + i).getColumn()) != null ? row.getCell(new CellAddress("W" + i).getColumn()).getNumericCellValue() : 0;
                Double priceZh = row.getCell(new CellAddress("N" + i).getColumn()) != null ? row.getCell(new CellAddress("N" + i).getColumn()).getNumericCellValue() : 0;
                Double priceRetail = row.getCell(new CellAddress("X" + i).getColumn()) != null ? row.getCell(new CellAddress("X" + i).getColumn()).getNumericCellValue() : 0;
                Double vip1 = row.getCell(new CellAddress("Y" + i).getColumn()) != null ? row.getCell(new CellAddress("Y" + i).getColumn()).getNumericCellValue() : 0;
                Double vip2 = row.getCell(new CellAddress("Z" + i).getColumn()) != null ? row.getCell(new CellAddress("Z" + i).getColumn()).getNumericCellValue() : 0;
                Double vip3 = row.getCell(new CellAddress("AA" + i).getColumn()) != null ? row.getCell(new CellAddress("AA" + i).getColumn()).getNumericCellValue() : 0;
                Double vip4 = row.getCell(new CellAddress("AB" + i).getColumn()) != null ? row.getCell(new CellAddress("AB" + i).getColumn()).getNumericCellValue() : 0;
                String description = row.getCell(new CellAddress("AC" + i).getColumn()) != null ? row.getCell(new CellAddress("AC" + i).getColumn()).getStringCellValue() : null;
                Double weight = row.getCell(new CellAddress("AD" + i).getColumn()) != null ? row.getCell(new CellAddress("AD" + i).getColumn()).getNumericCellValue() : 0;
                String keywords = row.getCell(new CellAddress("AM" + i).getColumn()) != null ? row.getCell(new CellAddress("AM" + i).getColumn()).getStringCellValue() : null;

                /** */
                /** images */
                List<ImageEntity> images = imageRepository.findAllByProductSKU(productSku);
                if (images.size()<=0) continue;
                /***/
                /** category*/

                CategoryEntity categoryEntity = categoryRepository.findBySku(categorySku);
                List<CategoryEntity> parents = categoryService.getAllParent(categoryEntity.getId());
                parents.add(categoryEntity);
                /***/
                /** provider */
                ProviderEntity providers = providerRepository.findByHost(env.getProperty("spring.domain"));
                /***/
                ProductEntity productEntity = new ProductEntity();
                productEntity.setVariants(new ArrayList<>());
                productEntity.setVariantGroup(new ArrayList<>());
                if (productRepository.existsBySkuVi(productSku)){
                    productEntity = productRepository.findBySkuVi(productSku);
                }
                if (parents!=null && parents.size()>0){
                    productEntity.setCategories(parents);
                }
                productEntity.setNameZh(nameZh);
                productEntity.setSkuZh("Zh"+productSku);
                productEntity.setNameVi(name);
                productEntity.setSkuVi(productSku);
                productEntity.setSlugVi(ConvertStringToUrl.covertStringToURL(productEntity.getNameVi()+"-"+productEntity.getSkuVi()));
                productEntity.setThumbnail(images.get(0).getPath());
                productEntity.setGallery(images.stream().map(imageEntity -> imageEntity.getPath()).collect(Collectors.toList()));
                productEntity.setContent(generateContent(productEntity.getGallery()));
                productEntity.setStatus(1);
                productEntity.setTrash(false);
                productEntity.setConditiondefault(condition1.intValue());
                productEntity.setCondition1(condition2.intValue());
                productEntity.setCondition2(condition3.intValue());
                productEntity.setCondition3(condition3!=null?condition3.intValue()*2:null);
                productEntity.setCondition4(condition3!=null?condition3.intValue()*4:null);
                productEntity.setCategoryDefault(categoryEntity.getId());
                productEntity.setEnableAutoUpdatePrice(true);
                productEntity.setLink(null);
                productEntity.setUnit(unit);
                productEntity.setCategories(parents);
                productEntity.setDescription(description);
                productEntity.setProviders(List.of(providers));
                productEntity.setKeyword(keywords);
                productEntity = productRepository.save(productEntity);
                VariantEntity variantEntity = null;
                   if (variantChildSku!=null && variantChildSku!=""){
                       variantEntity = productEntity.getVariants().stream().filter(variantEntity1 -> variantEntity1.getSkuVi().equals(variantChildSku)).findAny().orElse(null);
                   }else {
                       variantEntity = productEntity.getVariants().stream().filter(variantEntity1 -> variantEntity1.getSkuVi().equals(variantSku)).findAny().orElse(null);
                   }
                if (variantEntity==null) variantEntity = new VariantEntity();
                variantEntity.setNameZh(variantChildSku==null?variantChildNameZh:variantNameZh);
                variantEntity.setNameVi(variantChildSku==null?variantChildName:variantName);
                variantEntity.setSkuZh(variantChildSku==null?"ZH"+variantChildSku:"ZH"+variantSku);
                variantEntity.setSkuVi(variantChildSku==null?variantChildSku:variantSku);
                variantEntity.setThumbnail(images.stream().filter(imageEntity -> imageEntity.getVariantSKU()!=null && imageEntity.getVariantSKU().equals(variantChildSku==""?variantSku:variantChildSku)).findAny().orElse(new ImageEntity()).getPath());
                variantEntity.setWeight(weight.floatValue());
                variantEntity.setStock(stock.intValue());
                variantEntity.setPrice(priceRetail.longValue());
                variantEntity.setVip1(vip1.longValue());
                variantEntity.setVip2(vip2.longValue());
                variantEntity.setVip3(vip3.longValue());
                variantEntity.setVip4(vip4.longValue());
                variantEntity.setCost(cost.longValue());
                variantEntity.setPriceZh(priceZh.doubleValue());
                variantEntity.setProductEntity(productEntity);
                variantRepository.save(variantEntity);

            }
        }


    }

    @Override
    public void downloadThumbnail(Integer categoryId) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(categoryId);
            /**foler*/
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-image.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            products.forEach(productEntity -> {
                byte[] bytes = getInputImage(productEntity.getThumbnail());
                if (bytes!=null){
                    ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"-"+FilenameUtils.getName(productEntity.getThumbnail()));
                    try {
                        zipOut.putNextEntry(zipEntry);
                        zipOut.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });
            zipOut.close();
            fos.close();
        }

    }

    @Override
    public void downloadThumbnailAddText(Integer categoryId) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(categoryId);
            /**foler*/
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-image-sku.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            products.forEach(productEntity -> {
                byte[] bytes = getInputImage(productEntity.getThumbnail());
                if (bytes!=null){
                    ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"-"+FilenameUtils.getName(productEntity.getThumbnail()));
                    try {
                        BufferedImage bufferedImage = ImageUtils.addText(productEntity.getSkuVi(),bytes);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, FilenameUtils.getExtension(productEntity.getThumbnail()), baos);
                        bytes = baos.toByteArray();
                        zipOut.putNextEntry(zipEntry);
                        zipOut.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            zipOut.close();
            fos.close();
        }
    }

    @Override
    public void downloadImagesAddText(Integer categoryId) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(categoryId);
            /**foler*/
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-images-sku.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            products.forEach(productEntity -> {
                productEntity.getGallery().forEach(s1 -> {
                    byte[] bytes = getInputImage(s1);
                    if (bytes!=null){
                        ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"/"+FilenameUtils.getName(s1));
                        try {
                            BufferedImage bufferedImage = ImageUtils.addText(productEntity.getSkuVi(),bytes);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(bufferedImage, FilenameUtils.getExtension(s1), baos);
                            bytes = baos.toByteArray();
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                productEntity.getVariants().forEach(variantEntity -> {
                    if (variantEntity.getThumbnail()!=null && !variantEntity.getThumbnail().equals("")){
                        byte[] bytes = getInputImage(variantEntity.getThumbnail());
                        if (bytes!=null){
                            ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"/"+variantEntity.getSkuVi()+"/"+FilenameUtils.getName(variantEntity.getThumbnail()));
                            try {
                                BufferedImage bufferedImage = ImageUtils.addText(variantEntity.getSkuVi(),bytes);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ImageIO.write(bufferedImage, FilenameUtils.getExtension(variantEntity.getThumbnail()), baos);
                                bytes = baos.toByteArray();
                                zipOut.putNextEntry(zipEntry);
                                zipOut.write(bytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            });
            zipOut.close();
            fos.close();
        }
    }

    @Override
    public void downloadImages(Integer categoryId) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        if (categoryEntity!=null){
            List<ProductEntity> products = productRepository.findAllByCategoryDefault(categoryId);
            /**foler*/
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/zip");
            if (!folder.exists()) folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-images.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            products.forEach(productEntity -> {
                productEntity.getGallery().forEach(s1 -> {
                    byte[] bytes = getInputImage(s1);
                    if (bytes!=null){
                        ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"/"+FilenameUtils.getName(s1));
                        try {
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                productEntity.getVariants().forEach(variantEntity -> {
                    if (variantEntity.getThumbnail()!=null && !variantEntity.getThumbnail().equals("")){
                        byte[] bytes = getInputImage(variantEntity.getThumbnail());
                        if (bytes!=null){
                            ZipEntry zipEntry = new ZipEntry(productEntity.getSkuVi()+"/"+variantEntity.getSkuVi()+"/"+FilenameUtils.getName(variantEntity.getThumbnail()));
                            try {
                                zipOut.putNextEntry(zipEntry);
                                zipOut.write(bytes);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            });
            zipOut.close();
            fos.close();
        }
    }

    private String generateContent(List<String> gallery) {
        UrlValidator urlValidator = new UrlValidator();
        StringBuilder builder = new StringBuilder("");
        for (String image : gallery) {
            builder.append("<figure class=\"image\"><img src=\"" + (urlValidator.isValid(image)?image:(env.getProperty("spring.domain")+ image )) + "\"></figure>");
        }
        return builder.toString();
    }
    @Override
    public void onChangeUpdateCategory(Integer categoryId) {
        List<ProductEntity> products = productRepository.findAllByCategoryDefault(categoryId);
        CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
        taskScheduler.schedule(()->{
            products.forEach(productEntity -> {
                productEntity.getVariants().forEach(variantEntity -> {
                    if (productEntity.getEnableAutoUpdatePrice() && categoryEntity != null) {
                        variantEntity.setPrice(NumberUtils.round(categoryEntity.getRate().longValue() * categoryEntity.getFactorDefault().longValue() * variantEntity.getPriceZh().longValue()));
                        variantEntity.setVip1(NumberUtils.round(categoryEntity.getRate().longValue() * categoryEntity.getFactor1().longValue() * variantEntity.getPriceZh().longValue()));
                        variantEntity.setVip2(NumberUtils.round(categoryEntity.getRate().longValue() * categoryEntity.getFactor2().longValue() * variantEntity.getPriceZh().longValue()));
                        variantEntity.setVip3(NumberUtils.round(categoryEntity.getRate().longValue() * categoryEntity.getFactor3().longValue() * variantEntity.getPriceZh().longValue()));
                        variantEntity.setVip4(NumberUtils.round(categoryEntity.getRate().longValue() * categoryEntity.getFactor4().longValue() * variantEntity.getPriceZh().longValue()));
                        variantRepository.save(variantEntity);
                    }
                });
            });
        },new Date());

    }

    @Override
    public String getQuickview(Integer id) throws JsonProcessingException {
        Context context = new Context();
        ProductUtilsDialect productUtilsDialect = new ProductUtilsDialect();
        PriceOption priceOption = settingService.getPriceSetting();
        ProductEntity productEntity = findById(id);
        Long countIncart = cartService.countByProduct(productEntity.getId());
        Boolean isRetailt = ProductUtils.isRetailt(productEntity,priceOption);
        Long max = productUtilsDialect.getPriceByTypeNF(priceOption.getPriceDefault(),productEntity,"max");
        Long min = productUtilsDialect.getPriceByTypeNF(priceOption.getPriceDefault(),productEntity,"min");
        Long vip1 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice1(),productEntity);
        Long vip2 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice2(),productEntity);
        Long vip3 = productUtilsDialect.getPriceByTypeNF(priceOption.getPrice3(),productEntity);

        Map map = new HashMap();
        map.put("id",productEntity.getId());
        map.put("min",min);
        map.put("max",max);
        map.put("incart",countIncart);
        map.put("isRetailt", isRetailt);
        map.put("vip1",vip1);
        map.put("vip2",vip2);
        map.put("vip3",vip3);
        map.put("conditiondefault",productEntity.getConditiondefault());
        map.put("condition1",productEntity.getCondition1());
        map.put("condition2",productEntity.getCondition2());
        map.put("condition3",productEntity.getCondition3());
        map.put("isExistGroup",false);
        String jsonProduct = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(map);
        context.setVariable("jsonproduct",jsonProduct);
        context.setVariables(map);
        context.setVariable("product",productEntity);
        context.setVariable("priceSetting",priceOption);
        if (productEntity.getVariantGroup()!=null){
            if (productEntity.getVariantGroup().size()>0){
                context.setVariable("isExistGroup",true);
            }
        }
        String html = templateEngine.process("fragment/product/quickview",context);
        return html;
    }

    @Override
    public void deleteAllByCat(Integer catId) {
        List<ProductEntity> products = productRepository.findAllByCategory(catId);
        productRepository.deleteAll(products);
    }

    @Override
    public void generatePriceQuoteExcel(Integer category) throws IOException {
        CategoryEntity categoryEntity = categoryRepository.findById(category).orElse(null);
        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath()+"/excel");
        FileOutputStream outputStream = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-bao-gia.xlsx");
        List<ProductEntity> products = productRepository.findAllByCategoryDefault(category);
        XSSFWorkbook ouputbook = new XSSFWorkbook();
        XSSFSheet outSheet = ouputbook.createSheet("ngon");
        UrlValidator validator = new UrlValidator();
        int k = 1;
        createHeaderSapo1(outSheet.createRow(0));
        for (ProductEntity productEntity : products) {
            for (VariantEntity variationEntity : productEntity.getVariants()) {
                if (variationEntity.getStock()>0){
                    Row cells = outSheet.createRow(k++);
                    cells.createCell(new CellAddress("A1").getColumn()).setCellValue(productEntity.getNameVi() + "-" + variationEntity.getNameVi());
//                    cells.createCell(new CellAddress("B1").getColumn()).setCellValue("Sản phẩm thường");
                    cells.createCell(new CellAddress("C1").getColumn()).setCellValue(productEntity.getSkuVi());
//                    cells.createCell(new CellAddress("D1").getColumn()).setCellValue(productEntity.getDescription());
                    cells.createCell(new CellAddress("E1").getColumn()).setCellValue("BHNGOCMINH");
//                    cells.createCell(new CellAddress("F1").getColumn()).setCellValue("Tags");
                    cells.createCell(new CellAddress("G1").getColumn()).setCellValue("kích thước");
                    cells.createCell(new CellAddress("H1").getColumn()).setCellValue(variationEntity.getDimension());
//                    cells.createCell(new CellAddress("I1").getColumn()).setCellValue("Trọng lượng");
//                    cells.createCell(new CellAddress("J1").getColumn()).setCellValue(variationEntity.getWeight());
//                    cells.createCell(new CellAddress("K1").getColumn()).setCellValue("Chất liệu");
//                    cells.createCell(new CellAddress("L1").getColumn()).setCellValue(productEntity.getMaterial());
//                    cells.createCell(new CellAddress("M1").getColumn()).setCellValue("Tên phiên bản sản phẩm");
                    cells.createCell(new CellAddress("N1").getColumn()).setCellValue(variationEntity.getSkuVi());
                    cells.createCell(new CellAddress("O1").getColumn()).setCellValue(variationEntity.getSkuVi());
                    cells.createCell(new CellAddress("P1").getColumn()).setCellValue(variationEntity.getWeight()!=null?variationEntity.getWeight():0);
                    cells.createCell(new CellAddress("Q1").getColumn()).setCellValue("g");
                    if (variationEntity.getThumbnail() != null && !variationEntity.getThumbnail().equalsIgnoreCase("")) {
                        cells.createCell(new CellAddress("R1").getColumn()).setCellValue(env.getProperty("spring.domain") + variationEntity.getThumbnail());
                    } else {
                        cells.createCell(new CellAddress("R1").getColumn()).setCellValue(env.getProperty("spring.domain") + productEntity.getThumbnail());
                    }
                    cells.createCell(new CellAddress("S1").getColumn()).setCellValue(productEntity.getUnit());
                    cells.createCell(new CellAddress("T1").getColumn()).setCellValue("Có");
                    cells.createCell(new CellAddress("U1").getColumn()).setCellValue(variationEntity.getCost()!=null?variationEntity.getCost():0);
                    cells.createCell(new CellAddress("V1").getColumn()).setCellValue(variationEntity.getStock());
                    cells.createCell(new CellAddress("W1").getColumn()).setCellValue(0);
                    cells.createCell(new CellAddress("X1").getColumn()).setCellValue(100000);
                    cells.createCell(new CellAddress("Y1").getColumn()).setCellValue("Kệ A");
                    cells.createCell(new CellAddress("Z1").getColumn()).setCellValue(0);
                    cells.createCell(new CellAddress("AA1").getColumn()).setCellValue(variationEntity.getPrice());
//                    cells.createCell(new CellAddress("AB1").getColumn()).setCellValue(variationEntity.getVip1());
//                    cells.createCell(new CellAddress("AC1").getColumn()).setCellValue(variationEntity.getVip2());
//                    cells.createCell(new CellAddress("AD1").getColumn()).setCellValue(variationEntity.getVip3());
//                    cells.createCell(new CellAddress("AE1").getColumn()).setCellValue(variationEntity.getVip4());
                    int index = new CellAddress("AF1").getColumn();
                    int max = 0;
                    for (String image:productEntity.getGallery()){
                        if (max>=5){
                            break;
                        }
                        cells.createCell(index++).setCellValue(validator.isValid(image)?image:env.getProperty("spring.domain")+image);
                        max++;
                    }
                }

            }
        }
        ouputbook.write(outputStream);
        ouputbook.close();
        outputStream.close();

    }

    @Override
    public void generatePriceQuoteExcelWithThumbnail(Integer category) throws Exception {

        CategoryEntity categoryEntity = categoryRepository.findById(category).orElse(null);
        File s = UploadFileUtils.getPath("static/");
        File folder = new File(s.getPath()+"/excel");
        FileOutputStream outputStream = new FileOutputStream(folder.getPath()+"/"+categoryEntity.getSlug()+"-bao-gia-kem-hinh-anh.xlsx");
        List<ProductEntity> products = productRepository.findAllByCategoryDefault(category);
        XSSFWorkbook ouputbook = new XSSFWorkbook();
        XSSFSheet outSheet = ouputbook.createSheet("ngon");
        int k = 1;
        createHeaderSapo2(outSheet.createRow(0));
        for (ProductEntity productEntity : products) {
            if (productEntity.getVariantGroup()!=null && productEntity.getVariantGroup().size()>0){
                for (GroupVariantEntity groupVariantEntity:productEntity.getVariantGroup()){
                    for (VariantEntity variationEntity : productEntity.getVariants()) {
                       if (groupVariantEntity.getSkuGroup().equals(variationEntity.getParent())){
                           Row cells = outSheet.createRow(k++);
                           if (variationEntity.getThumbnail()!=null && !variationEntity.getThumbnail().equals("")){
                               addPicture(variationEntity.getThumbnail(),cells,ouputbook,outSheet);
                           }else {
                               addPicture(productEntity.getThumbnail(),cells,ouputbook,outSheet);
                           }
                           Cell skuCell = cells.createCell(new CellAddress("B1").getColumn());
                           Cell skuVariantCell = cells.createCell(new CellAddress("C1").getColumn());
                           Cell skuVariantChildCell = cells.createCell(new CellAddress("D1").getColumn());
                           Cell productName = cells.createCell(new CellAddress("E1").getColumn());
                           Cell variantName = cells.createCell(new CellAddress("F1").getColumn());
                           Cell variantChildName = cells.createCell(new CellAddress("G1").getColumn());
                           Cell unit = cells.createCell(new CellAddress("H1").getColumn());
                           Cell condition1 = cells.createCell(new CellAddress("I1").getColumn());
                           Cell condition2 = cells.createCell(new CellAddress("J1").getColumn());
                           Cell condition3 = cells.createCell(new CellAddress("K1").getColumn());
                           Cell price = cells.createCell(new CellAddress("L1").getColumn());
                           Cell price1 = cells.createCell(new CellAddress("M1").getColumn());
                           Cell price2 = cells.createCell(new CellAddress("N1").getColumn());
                           Cell price3 = cells.createCell(new CellAddress("O1").getColumn());
                           skuCell.setCellValue(productEntity.getSkuVi());
                           skuVariantCell.setCellValue(groupVariantEntity.getSkuGroup());
                           skuVariantChildCell.setCellValue(variationEntity.getSkuVi());
                           productName.setCellValue(productEntity.getNameVi());
                           variantName.setCellValue(groupVariantEntity.getName());
                           variantChildName.setCellValue(variationEntity.getNameVi());
                           unit.setCellValue(productEntity.getUnit());
                           condition1.setCellValue(productEntity.getConditiondefault());
                           if (productEntity.getCondition1()!=null)condition2.setCellValue(productEntity.getCondition1());
                           if (productEntity.getCondition2()!=null) condition3.setCellValue(productEntity.getCondition2());
                           price.setCellValue(variationEntity.getPrice());
                           price1.setCellValue(variationEntity.getVip1());
                           price2.setCellValue(variationEntity.getVip2());
                           price3.setCellValue(variationEntity.getVip3());

                       }
                    }
                }
            }else {
                for (VariantEntity variationEntity : productEntity.getVariants()) {
                    Row cells = outSheet.createRow(k++);
                    if (variationEntity.getThumbnail()!=null && !variationEntity.getThumbnail().equals("")){
                        addPicture(variationEntity.getThumbnail(),cells,ouputbook,outSheet);
                    }else {
                        addPicture(productEntity.getThumbnail(),cells,ouputbook,outSheet);
                    }
                    Cell skuCell = cells.createCell(new CellAddress("B1").getColumn());
                    Cell skuVariantCell = cells.createCell(new CellAddress("C1").getColumn());
                    Cell productName = cells.createCell(new CellAddress("E1").getColumn());
                    Cell variantName = cells.createCell(new CellAddress("F1").getColumn());
                    Cell unit = cells.createCell(new CellAddress("H1").getColumn());
                    Cell condition1 = cells.createCell(new CellAddress("I1").getColumn());
                    Cell condition2 = cells.createCell(new CellAddress("J1").getColumn());
                    Cell condition3 = cells.createCell(new CellAddress("K1").getColumn());
                    Cell price = cells.createCell(new CellAddress("L1").getColumn());
                    Cell price1 = cells.createCell(new CellAddress("M1").getColumn());
                    Cell price2 = cells.createCell(new CellAddress("N1").getColumn());
                    Cell price3 = cells.createCell(new CellAddress("O1").getColumn());
                    skuCell.setCellValue(productEntity.getSkuVi());
                    skuVariantCell.setCellValue(variationEntity.getSkuVi());
                    productName.setCellValue(productEntity.getNameVi());
                    variantName.setCellValue(variationEntity.getNameVi());
                    unit.setCellValue(productEntity.getUnit());
                    condition1.setCellValue(productEntity.getConditiondefault());
                    if (productEntity.getCondition1()!=null)condition2.setCellValue(productEntity.getCondition1());
                    if (productEntity.getCondition2()!=null) condition3.setCellValue(productEntity.getCondition2());
                    price.setCellValue(variationEntity.getPrice());
                    price1.setCellValue(variationEntity.getVip1());
                    price2.setCellValue(variationEntity.getVip2());
                    price3.setCellValue(variationEntity.getVip3());
                }
            }

        }
        ouputbook.write(outputStream);
        ouputbook.close();
        outputStream.close();
    }

    private Picture addPicture(String url, Row row, XSSFWorkbook workbook, XSSFSheet sheet) throws Exception {
        /*** image * */
        //Get the contents of an InputStream as a byte[].
        byte[] input = getInputImage(url);
        if (input!=null){
            InputStream imageInput = new ByteArrayInputStream(input);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = IOUtils.toByteArray(imageInput);
            BufferedImage bufferedImage = ImageUtils.cropAndResize(bytes,200);
            ImageIO.write(bufferedImage,FilenameUtils.getExtension(url),byteArrayOutputStream);
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
    @Override
    public void updateCategory(Integer categoryId) {
        taskScheduler.schedule(() -> {
            List<CategoryEntity> categoryEntities = categoryService.getAllParent(categoryId);
            CategoryEntity categoryEntity = categoryRepository.findById(categoryId).orElse(null);
            if (categoryEntity!=null){
                categoryEntities.add(categoryEntity);
            }
            List<ProductEntity> productEntities = productRepository.findAllByCategoryDefault(categoryId);
            productEntities.forEach(productEntity -> {
                productEntity.setCategories(categoryEntities);
                productRepository.save(productEntity);
            });
        },new Date());
    }

    @Override
    public List<ProductDTO> getProductFromCache(Integer page, Integer pageSize) {
        return findRandForProductDTO(PageRequest.of(page,pageSize)).getContent();
    }

    @Override
    public void removeproductFromCache() {

    }

    @Override
    public Page<ProductDTO> search(String keyword, String slug, Pageable pageable) {
        String host = env.getProperty("spring.domain");
        Page<ProductDTO> productDTOS = productRepository.search(keyword,slug,host,"",pageable);
        for (ProductDTO productDTO : productDTOS.getContent()) {
            productDTO.setSlug(getFullURL(productDTO.getSlug(), productDTO.getCategorySlug()));
        }
        return productDTOS;
    }

    @Override
    public List<String> findAllKeywordByKeyword(String keyword, String host) {

        return productRepository.findAllKeywordsByKeyword(keyword,host);
    }

    @Override
    public List<ProductEntity> findAllByCatAndHost(Integer catId, String host) {
        return productRepository.findAllByCatAndHost(catId,host);
    }

    @Override
    public List<ProductEntity> findAllByCat(Integer catId) {
        return productRepository.findAllByCat(catId);
    }
    private void createHeaderSapo2(Row row) {
        Row cells = row;
        cells.createCell(new CellAddress("A1").getColumn()).setCellValue("Hình ảnh");
        cells.createCell(new CellAddress("B1").getColumn()).setCellValue("SKU");
        cells.createCell(new CellAddress("C1").getColumn()).setCellValue("SKU biến thể");
        cells.createCell(new CellAddress("D1").getColumn()).setCellValue("SKU biến thể con");
        cells.createCell(new CellAddress("E1").getColumn()).setCellValue("Tên sản phẩm");
        cells.createCell(new CellAddress("F1").getColumn()).setCellValue("Tên biến thể");
        cells.createCell(new CellAddress("G1").getColumn()).setCellValue("Tên biến thể con");
        cells.createCell(new CellAddress("H1").getColumn()).setCellValue("Đơn vị");
        cells.createCell(new CellAddress("I1").getColumn()).setCellValue("Điều kiện 1");
        cells.createCell(new CellAddress("J1").getColumn()).setCellValue("Điều kiện 2");
        cells.createCell(new CellAddress("K1").getColumn()).setCellValue("Điều kiện 3");
        cells.createCell(new CellAddress("L1").getColumn()).setCellValue("Bán lẻ");
        cells.createCell(new CellAddress("M1").getColumn()).setCellValue("Vip 1");
        cells.createCell(new CellAddress("N1").getColumn()).setCellValue("Vip 2");
        cells.createCell(new CellAddress("O1").getColumn()).setCellValue("Vip 3");


    }
    private void createHeaderSapo1(Row row) {
        Row cells = row;
        cells.createCell(new CellAddress("A1").getColumn()).setCellValue("Tên sản phẩm*");
        cells.createCell(new CellAddress("B1").getColumn()).setCellValue("Hình thức quản lý sản phẩm");
        cells.createCell(new CellAddress("C1").getColumn()).setCellValue("Mã loại sản phẩm");
        cells.createCell(new CellAddress("D1").getColumn()).setCellValue("Mô tả sản phẩm");
        cells.createCell(new CellAddress("E1").getColumn()).setCellValue("Nhãn hiệu");
        cells.createCell(new CellAddress("F1").getColumn()).setCellValue("Tags");
        cells.createCell(new CellAddress("G1").getColumn()).setCellValue("Thuộc tính 1");
        cells.createCell(new CellAddress("H1").getColumn()).setCellValue("Giá trị thuộc tính 1");
        cells.createCell(new CellAddress("I1").getColumn()).setCellValue("Thuộc tính 2");
        cells.createCell(new CellAddress("J1").getColumn()).setCellValue("Giá trị thuộc tính 2");
        cells.createCell(new CellAddress("K1").getColumn()).setCellValue("Thuộc tính 3");
        cells.createCell(new CellAddress("L1").getColumn()).setCellValue("Giá trị thuộc tính 3");
        cells.createCell(new CellAddress("M1").getColumn()).setCellValue("Tên phiên bản sản phẩm");
        cells.createCell(new CellAddress("N1").getColumn()).setCellValue("Mã SKU*");
        cells.createCell(new CellAddress("O1").getColumn()).setCellValue("Barcode");
        cells.createCell(new CellAddress("P1").getColumn()).setCellValue("Khối lượng");
        cells.createCell(new CellAddress("Q1").getColumn()).setCellValue("Đơn vị khối lượng");
        cells.createCell(new CellAddress("R1").getColumn()).setCellValue("Ảnh đại diện");
        cells.createCell(new CellAddress("S1").getColumn()).setCellValue("Đơn vị");
        cells.createCell(new CellAddress("T1").getColumn()).setCellValue("Áp dụng thuế");
        cells.createCell(new CellAddress("U1").getColumn()).setCellValue("LC_CN1_Giá vốn khởi tạo*");
        cells.createCell(new CellAddress("V1").getColumn()).setCellValue("LC_CN1_Tồn kho ban đầu*");
        cells.createCell(new CellAddress("W1").getColumn()).setCellValue("LC_CN1_Tồn tối thiểu");
        cells.createCell(new CellAddress("X1").getColumn()).setCellValue("LC_CN1_Tồn tối đa");
        cells.createCell(new CellAddress("Y1").getColumn()).setCellValue("LC_CN1_Điểm lưu kho");
        cells.createCell(new CellAddress("Z1").getColumn()).setCellValue("PL_Giá nhập");
        cells.createCell(new CellAddress("AA1").getColumn()).setCellValue("PL_Giá bán lẻ");
        cells.createCell(new CellAddress("AB1").getColumn()).setCellValue("PL_Giá bán buôn");
        cells.createCell(new CellAddress("AC1").getColumn()).setCellValue("PL_vip2");
        cells.createCell(new CellAddress("AD1").getColumn()).setCellValue("PL_vip3");
        cells.createCell(new CellAddress("AE1").getColumn()).setCellValue("PL_vip4");
        cells.createCell(new CellAddress("AF1").getColumn()).setCellValue("HA1");
        cells.createCell(new CellAddress("AG1").getColumn()).setCellValue("HA2");
        cells.createCell(new CellAddress("AH1").getColumn()).setCellValue("HA3");
        cells.createCell(new CellAddress("AI1").getColumn()).setCellValue("HA4");
        cells.createCell(new CellAddress("AJ1").getColumn()).setCellValue("HA5");

    }
    private String getFullURL(String s, String categorySlug) {
        String domain = env.getProperty("spring.domain");
        StringBuilder builder = new StringBuilder(domain);
        return builder.append("/").append(categorySlug).append("/").append(s).toString();
    }

    private String translate(String src) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://microsoft-translator-text.p.rapidapi.com/translate?api-version=3.0&to=vi&textType=plain&profanityAction=NoAction"))
                .header("content-type", "application/json")
                .header("x-rapidapi-key", "22799d8ad8msh172c5ca87d072b8p17e5c1jsne6e33bf0b807")
                .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("[{\"Text\": \""+src+"\"}]"))
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            List<Map> maps = (List<Map>) SpringUtils.convertJsonToObject(response.body(),List.class);
            if (maps!=null && maps.size()>0){
                List<Map> translations = (List) maps.get(0).get("translations");
                if (translations!=null){
                    return String.valueOf(translations.get(0).get("text"));
                }
            }
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }

        return src;
    }
    private byte[] getInputImage(String url) {
        UrlValidator urlValidator = new UrlValidator();
        if (urlValidator.isValid(url)){
            try {
                ResponseEntity<byte[]> inputImage = restTemplate.getForEntity(url, byte[].class);
                if (inputImage.getStatusCodeValue() == 200) {
                    return inputImage.getBody();
                }
            }catch (HttpClientErrorException httpClientErrorException){
                httpClientErrorException.printStackTrace();
            }
        }else {
            InputStream inputStream = null;
            try {
                File s = UploadFileUtils.getPath("static/");
                File folder = new File(s.getPath()+"/upload");
                File image = new File(folder.getPath()+"/"+ FilenameUtils.getName(url));
                inputStream = new FileInputStream(image);
                return inputStream.readAllBytes();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
