package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.controller.requests.cart.CartDetailtRequest;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.dto.cart.CartCategoryDTO;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.dto.cart.CartProductDTO;
import com.nomi.caysenda.dto.cart.CartVariantDTO;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.redis.model.RedisCartProduct;
import com.nomi.caysenda.redis.model.RedisCartVariant;
import com.nomi.caysenda.redis.model.RedisDeliveryFee;
import com.nomi.caysenda.redis.repositories.RedisCartProductRepository;
import com.nomi.caysenda.redis.repositories.RedisCartVariantRepository;
import com.nomi.caysenda.redis.repositories.RedisDeliveryFeeRepository;
import com.nomi.caysenda.repositories.CategoryRepository;
import com.nomi.caysenda.repositories.ProductRepository;
import com.nomi.caysenda.repositories.VariantRepository;
import com.nomi.caysenda.services.CartRedisService;
import com.nomi.caysenda.services.SettingService;
import com.nomi.caysenda.utils.ProductUtils;
import com.nomi.caysenda.utils.model.ProductRangeAndRetail;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CartRedisServiceImpl implements CartRedisService {
    @Autowired
    RedisCartProductRepository redisCartProductRepository;
    @Autowired
    RedisCartVariantRepository redisCartVariantRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    VariantRepository variantRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RedisDeliveryFeeRepository deliveryFeeRepository;
    @Autowired
    SettingService settingService;
    @Override
    public CartRequestResponese addtocart(CartRequest request) throws AddToCartException {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

        RedisCartProduct redisCartProduct = redisCartProductRepository.findBySessionIdAndProductId(sessionId,request.getProductId());
        PriceOption priceOption = settingService.getPriceSetting();
        ProductEntity productEntity = productRepository.findById(request.getProductId()).get();
        if (redisCartProduct!=null){
            for (CartDetailtRequest detailtRequest:request.getDetailts()){
                RedisCartVariant cartVariant = redisCartVariantRepository.findByProductCartIdAndVariantId(redisCartProduct.getId(),detailtRequest.getVariantId());
                VariantEntity variantEntity = variantRepository.findById(detailtRequest.getVariantId()).get();
                if (cartVariant!=null){
                    if (variantEntity.getStock()<cartVariant.getQuantity()+detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                    cartVariant.setQuantity(cartVariant.getQuantity()+detailtRequest.getQuantity());
                }else {
                    if (variantEntity.getStock()<detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                    cartVariant = new RedisCartVariant();
                    cartVariant.setVariantId(variantEntity.getId());
                    cartVariant.setName(variantEntity.getNameVi());
                    cartVariant.setThumbnail(variantEntity.getThumbnail());
                    cartVariant.setSku(variantEntity.getSkuVi());
                    cartVariant.setPriceDefault(variantEntity.getPrice());
                    cartVariant.setQuantity(detailtRequest.getQuantity());
                    cartVariant.setVip1(variantEntity.getVip1());
                    cartVariant.setVip2(variantEntity.getVip2());
                    cartVariant.setVip3(variantEntity.getVip3());
                    cartVariant.setVip4(variantEntity.getVip4());
                    cartVariant.setActive(false);
                    cartVariant.setCost(variantEntity.getCost());
                    cartVariant.setWeight(variantEntity.getWeight()!=null?variantEntity.getWeight():0);
                    cartVariant.setProductCartId(redisCartProduct.getId());
                    /** group */
                    GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                    if (groupVariantEntity!=null){
                        cartVariant.setGroupName(groupVariantEntity.getName());
                        cartVariant.setGroupZhName(groupVariantEntity.getZhName());
                        cartVariant.setGroupSku(groupVariantEntity.getSkuGroup());
                    }
                    /** end group */
                }
                redisCartVariantRepository.save(cartVariant);
            }
        }else {
            Long total = request.getDetailts().stream().mapToLong(value -> value.getQuantity()).sum();
            checkCondition(total,productEntity.getConditiondefault());
            CategoryEntity categoryEntity = categoryRepository.findById(productEntity.getCategoryDefault()).orElse(null);
            redisCartProduct = new RedisCartProduct();
            redisCartProduct.setSessionId(sessionId);
            redisCartProduct.setProductId(productEntity.getId());
            redisCartProduct.setName(productEntity.getNameVi());
            redisCartProduct.setSku(productEntity.getSkuVi());
            redisCartProduct.setSlug(productEntity.getSlugVi());
            redisCartProduct.setConditionDefault(productEntity.getConditiondefault());
            redisCartProduct.setCondition1(productEntity.getCondition1());
            redisCartProduct.setCondition2(productEntity.getCondition2());
            redisCartProduct.setCondition3(productEntity.getCondition3());
            redisCartProduct.setCondition4(productEntity.getCondition4());
            redisCartProduct.setThumbnail(productEntity.getThumbnail());
            redisCartProduct.setCategoryId(productEntity.getCategoryDefault());
            redisCartProduct.setTypeDefault(categoryEntity.getTypeDefault());
            redisCartProduct.setType1(categoryEntity.getType1());
            redisCartProduct.setType2(categoryEntity.getType2());
            redisCartProduct.setType3(categoryEntity.getType3());
            redisCartProduct.setRetail(ProductUtils.isRetailt(productEntity,priceOption));
            redisCartProduct.setLinkZh(productEntity.getLink());
            redisCartProduct.setNameZh(productEntity.getNameZh());
            redisCartProduct.setUnit(productEntity.getUnit());
            redisCartProduct.setTopFlag(productEntity.getTopFlag());
            redisCartProduct = redisCartProductRepository.save(redisCartProduct);
            for (CartDetailtRequest detailtRequest: request.getDetailts()){
                VariantEntity variantEntity = variantRepository.findById(detailtRequest.getVariantId()).get();
                if (variantEntity.getStock()<detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                RedisCartVariant cartVariant = new RedisCartVariant();
                cartVariant.setVariantId(variantEntity.getId());
                cartVariant.setName(variantEntity.getNameVi());
                cartVariant.setThumbnail(variantEntity.getThumbnail());
                cartVariant.setSku(variantEntity.getSkuVi());
                cartVariant.setPriceDefault(variantEntity.getPrice());
                cartVariant.setQuantity(detailtRequest.getQuantity());
                cartVariant.setVip1(variantEntity.getVip1());
                cartVariant.setVip2(variantEntity.getVip2());
                cartVariant.setVip3(variantEntity.getVip3());
                cartVariant.setVip4(variantEntity.getVip4());
                cartVariant.setNameZh(variantEntity.getNameZh());
                cartVariant.setActive(false);
                cartVariant.setCost(variantEntity.getCost());
                cartVariant.setWeight(variantEntity.getWeight()!=null?variantEntity.getWeight():0);
                cartVariant.setProductCartId(redisCartProduct.getId());
                /** group */
                GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                if (groupVariantEntity!=null){
                    cartVariant.setGroupName(groupVariantEntity.getName());
                    cartVariant.setGroupZhName(groupVariantEntity.getZhName());
                    cartVariant.setGroupSku(groupVariantEntity.getSkuGroup());
                }
                /** end group */
               redisCartVariantRepository.save(cartVariant);
            }
        }
        return null;
    }

    @Override
    public Boolean checkCondition(Long total, Integer condition) throws AddToCartException {
        PriceOption priceOption = settingService.getPriceSetting();
        if (priceOption.getEnableRetail()){
            if (total<0) throw new AddToCartException(AddToCartException.ENOUGH_CONDITION,AddToCartException.ENOUGH_CONDITION_MESSAGE);
        }else {
            if (total<condition) throw new AddToCartException(AddToCartException.ENOUGH_CONDITION,AddToCartException.ENOUGH_CONDITION_MESSAGE);
        }
        return true;
    }

    @Override
    public CartDTO getCart() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        List<RedisCartProduct> products = redisCartProductRepository.findAllBySessionId(sessionId);
        PriceOption priceOption = settingService.getPriceSetting();
        CartDTO cartDTO = new CartDTO();
        List<CartProductDTO> cartProductDTOS = convertRedisCartProductToCartProductDTO(products);
        for (CartProductDTO productDTO:cartProductDTOS){
            List<CartVariantDTO> variants = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(productDTO.getId()));
            productDTO.setVariants(variants);
            productDTO.setWeight(variants.stream().filter(cartVariantDTO ->
                    cartVariantDTO.getActive()).mapToDouble(cartVariantDTO ->
                    cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight()*cartVariantDTO.getQuantity():0).sum());

            ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
            ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        }

        Map<Integer,List<CartProductDTO>> group = cartProductDTOS.stream().collect(groupingBy(CartProductDTO::getCategoryId));
        List<CartCategoryDTO> categories = new ArrayList<>();
        group.entrySet().forEach((integerListEntry -> {
            CategoryEntity categoryEntity = categoryRepository.findById(integerListEntry.getKey()).get();
            Boolean active = true;
            Long amount = Long.valueOf(0);
            Long amountActive = Long.valueOf(0);
            for (CartProductDTO cartProductDTO:integerListEntry.getValue()){
                if (!cartProductDTO.getActive()) active=false;
                amount+=cartProductDTO.getAmount();
                amountActive+=cartProductDTO.getAmountActive();
            }
            CartCategoryDTO cartProductDTO = new CartCategoryDTO(integerListEntry.getKey(),categoryEntity.getName(),categoryEntity.getSlug(),(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0),integerListEntry.getValue());
            cartProductDTO.setActive(active);
            cartProductDTO.setAmount(amount);
            cartProductDTO.setAmountActive(amountActive);
            categories.add(cartProductDTO);
        }));
        cartDTO.setCategories(categories);
        return cartDTO;
    }
    CartProductDTO convertRedisCartProductToCartProductDTO(RedisCartProduct redisCartProduct){
        CartProductDTO cartProductDTO = new CartProductDTO();
        ModelMapper mapper = new ModelMapper();
        mapper.map(redisCartProduct,cartProductDTO);
        return cartProductDTO;
    }
    List<CartProductDTO> convertRedisCartProductToCartProductDTO(List<RedisCartProduct> redisCartProducts){
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<CartProductDTO>>(){}.getType();
        return mapper.map(redisCartProducts,listType);
    }
    List<CartVariantDTO> convertRedisCartVatiantToCartVariantDTO(List<RedisCartVariant> redisCartVariants){
        ModelMapper mapper = new ModelMapper();
        Type listType = new TypeToken<List<CartVariantDTO>>(){}.getType();
        return mapper.map(redisCartVariants,listType);
    }


    @Override
    public Long countByProduct(Integer productId) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        RedisCartProduct redisCartProduct = redisCartProductRepository.findBySessionIdAndProductId(sessionId,productId);
        if (redisCartProduct!=null){
            List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartId(redisCartProduct.getId());
            Long quantity = variants.stream().mapToLong((redisCartVariant -> redisCartVariant.getQuantity())).sum();
            return quantity;
        }
        return Long.valueOf(0);
    }

    @Override
    public CartRequestResponese update(Long productId, Long variantId, Integer quantity) throws AddToCartException {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        AtomicReference<Integer> totalQuantity = new AtomicReference<>(0);
        List<RedisCartVariant> variantEntities = redisCartVariantRepository.findAllByProductCartId(productId);
        variantEntities.forEach(variantDTO -> {
            if (!variantDTO.getId().equals(variantId)){
                totalQuantity.updateAndGet(v -> v + variantDTO.getQuantity());
            }
        });
        RedisCartProduct redisCartProduct = redisCartProductRepository.findById(productId).orElse(null);
        RedisCartVariant cartVariant = redisCartVariantRepository.findById(variantId).get();
        PriceOption priceOption = settingService.getPriceSetting();
        if (cartVariant!=null){
            if (totalQuantity.get()+quantity<redisCartProduct.getConditionDefault()) throw new AddToCartException(AddToCartException.ENOUGH_CONDITION,AddToCartException.ENOUGH_CONDITION_MESSAGE,cartVariant.getQuantity());
            VariantEntity variantEntity = variantRepository.findById(cartVariant.getVariantId()).get();
            if (quantity>variantEntity.getStock()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
            cartVariant.setQuantity(quantity);
            redisCartVariantRepository.save(cartVariant);
        }
        CartProductDTO productDTO = convertRedisCartProductToCartProductDTO(redisCartProduct);
        List<CartVariantDTO> variants = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(productDTO.getId()));
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);

        /** check active product*/
        CategoryEntity categoryEntity = categoryRepository.findById(redisCartProduct.getCategoryId()).orElse(null);
        List<CartProductDTO> products = convertRedisCartProductToCartProductDTO(redisCartProductRepository.findAllByCategoryIdAndSessionId(categoryEntity.getId(),sessionId));
        Long amountCategory = productDTO.getAmount();
        for (CartProductDTO cartProductDTO:products){
            if (!cartProductDTO.getId().equals(productId)){
                List<CartVariantDTO> variantDTOS = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(cartProductDTO.getId()));
                cartProductDTO.setVariants(variantDTOS);
                cartProductDTO.setWeight(variantDTOS.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
                ProductRangeAndRetail productRangeAndRetail1 = ProductUtils.isRetailt(cartProductDTO,priceOption);
                ProductUtils.getFullDataCartProductDTO(cartProductDTO,productRangeAndRetail1,priceOption);
                amountCategory+=cartProductDTO.getAmountActive();
            }

        }
        Boolean activeProduct = false;
        if (amountCategory>=(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0)){
            if (variants.size()>0 && !variants.get(0).getActive()){
                activeByProduct(categoryEntity.getId(),productId,true);

            }
            activeProduct = true;
            productDTO.setAmountActive(productDTO.getAmount());
        }else {
            if (variants.size()>0 && variants.get(0).getActive()){
                activeByCat(categoryEntity.getId(),false);
            }
            productDTO.setAmountActive(Long.valueOf(0));
        }

        return new CartRequestResponese(true,"Cập nhật thành công",List.of(productDTO),activeProduct,amountCategory);
    }

    @Override
    public CartRequestResponese activeAll(Boolean active) {
        CartDTO cartDTO = getCart();
        PriceOption priceOption = settingService.getPriceSetting();
        for (CartCategoryDTO cartCategoryDTO:cartDTO.getCategories()){
            if (active){
                Boolean check = true;
                if (!priceOption.getEnableConditionCategory()){
                    Long amount = cartCategoryDTO.getProducts().stream().mapToLong((cartProductDTO -> cartProductDTO.getAmount())).sum();
                    check=amount>=cartCategoryDTO.getCondition();
                }
                for (CartProductDTO productDTO:cartCategoryDTO.getProducts()){
                    List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartId(productDTO.getId());

                    for (RedisCartVariant variantEntity:variants){
                        variantEntity.setActive(check);
                    }
                    redisCartVariantRepository.saveAll(variants);
                }
            }else {
                for (CartProductDTO productDTO:cartCategoryDTO.getProducts()){
                    List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartId(productDTO.getId());
                    for (RedisCartVariant variantEntity:variants){
                        variantEntity.setActive(active);
                    }
                    redisCartVariantRepository.saveAll(variants);
                }
            }

        }
        return new CartRequestResponese(true,"active success");
    }

    @Override
    public CartRequestResponese activeByCat(Integer cat, Boolean active) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        CategoryEntity categoryEntity = categoryRepository.findById(cat).orElse(null);
        List<CartProductDTO> products = convertRedisCartProductToCartProductDTO(redisCartProductRepository.findAllByCategoryIdAndSessionId(categoryEntity.getId(),sessionId));
        /***/
        /** item update*/
        AtomicReference<Long> amountCategory = new AtomicReference<>(Long.valueOf(0));
        products.forEach(productDTO -> {
            List<CartVariantDTO> variants = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(productDTO.getId()));
            variants.forEach(variantDTO -> {
                variantDTO.setActive(active);
            });
            productDTO.setVariants(variants);
            productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight():0).sum());
            ProductRangeAndRetail productRangeAndRetail1 = ProductUtils.isRetailt(productDTO,priceOption);
            ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail1,priceOption);
            amountCategory.updateAndGet(v -> v + productDTO.getAmountActive());
        });

        Boolean check = true;
        if (!priceOption.getEnableConditionCategory()){
            check = amountCategory.get()>=(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0);
        }
        Boolean finalCheck = check;
        products.forEach(productDTO -> {
            productDTO.getVariants().forEach(variantDTO -> {
                RedisCartVariant cartVariant = redisCartVariantRepository.findById(variantDTO.getId()).orElse(null);
                cartVariant.setActive(finalCheck);
                redisCartVariantRepository.save(cartVariant);

            });
        });
        if (check){
            return new CartRequestResponese(true,"active true");
        }else {
            return new CartRequestResponese(false,"active true");
        }
    }

    @Override
    public CartRequestResponese activeByProduct(Integer catId, Long product, Boolean active) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        CategoryEntity categoryEntity = categoryRepository.findById(catId).orElse(null);
        List<CartProductDTO> products = convertRedisCartProductToCartProductDTO(redisCartProductRepository.findAllByCategoryIdAndSessionId(categoryEntity.getId(),sessionId));
        /***/
        /** item update*/
        RedisCartProduct redisCartProduct = redisCartProductRepository.findById(product).orElse(null);
        CartProductDTO productDTO = convertRedisCartProductToCartProductDTO(redisCartProduct);
        List<CartVariantDTO> variants = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(productDTO.getId()));
        variants.forEach(variantDTO -> {
            variantDTO.setActive(active);
            RedisCartVariant cartVariant = redisCartVariantRepository.findById(variantDTO.getId()).orElse(null);
            cartVariant.setActive(active);
            redisCartVariantRepository.save(cartVariant);
        });
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        if (!priceOption.getEnableConditionCategory()){
            /** check condition*/
            Long amountCategory = productDTO.getAmountActive();
            for (CartProductDTO cartProductDTO:products){
                if (!cartProductDTO.getId().equals(product)){
                    List<CartVariantDTO> variantDTOS = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(cartProductDTO.getId()));
                    cartProductDTO.setVariants(variantDTOS);
                    cartProductDTO.setWeight(variantDTOS.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
                    ProductRangeAndRetail productRangeAndRetail1 = ProductUtils.isRetailt(cartProductDTO,priceOption);
                    ProductUtils.getFullDataCartProductDTO(cartProductDTO,productRangeAndRetail1,priceOption);
                    amountCategory+=cartProductDTO.getAmountActive();
                }
            }
            if (amountCategory>=(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0)){
                return new CartRequestResponese(true,"active true",List.of(productDTO),true);
            }else {
                activeByCat(catId,false);
                return new CartRequestResponese(false,"active false",false);
            }
        }else {
            return new CartRequestResponese(true,"active true",List.of(productDTO),true);
        }

    }

    @Override
    public CartRequestResponese activeByVariant(Integer catId, Long product, Long variant, Boolean active) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        CategoryEntity categoryEntity = categoryRepository.findById(catId).orElse(null);
        List<CartProductDTO> products = convertRedisCartProductToCartProductDTO(redisCartProductRepository.findAllByCategoryIdAndSessionId(categoryEntity.getId(),sessionId));
        /***/
        /** item update*/
        RedisCartProduct redisCartProduct = redisCartProductRepository.findById(product).orElse(null);
        CartProductDTO productDTO = convertRedisCartProductToCartProductDTO(redisCartProduct);
        List<CartVariantDTO> variants = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(productDTO.getId()));
        variants.forEach(variantDTO -> {
            if (variantDTO.getId().equals(variant)){
                variantDTO.setActive(active);
                RedisCartVariant cartVariant = redisCartVariantRepository.findById(variantDTO.getId()).orElse(null);
                cartVariant.setActive(active);
                redisCartVariantRepository.save(cartVariant);
            }
        });
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        if (!priceOption.getEnableConditionCategory()){
            /** check condition*/
            Long amountCategory = productDTO.getAmountActive();
            for (CartProductDTO cartProductDTO:products){
                if (!cartProductDTO.getId().equals(product)){
                    List<CartVariantDTO> variantDTOS = convertRedisCartVatiantToCartVariantDTO(redisCartVariantRepository.findAllByProductCartId(cartProductDTO.getId()));
                    cartProductDTO.setVariants(variantDTOS);
                    cartProductDTO.setWeight(variantDTOS.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()).sum());
                    ProductRangeAndRetail productRangeAndRetail1 = ProductUtils.isRetailt(cartProductDTO,priceOption);
                    ProductUtils.getFullDataCartProductDTO(cartProductDTO,productRangeAndRetail1,priceOption);
                    amountCategory+=cartProductDTO.getAmountActive();
                }
            }
            if (amountCategory>=(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0)){
                return new CartRequestResponese(true,"active true",List.of(productDTO),true);
            }else {
                activeByCat(catId,false);
                return new CartRequestResponese(false,"active false",false);
            }
        }else {
            return new CartRequestResponese(true,"active true",List.of(productDTO),true);
        }


    }

    @Override
    public CartRequestResponese removeByProduct(Long product) {
        redisCartProductRepository.deleteById(product);
        List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartId(product);
        if (variants.size()>0){
            redisCartVariantRepository.deleteAll(variants);
        }

        return new CartRequestResponese(true,"remove success");
    }

    @Override
    public CartRequestResponese removeAllActive() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        List<RedisCartProduct> products = redisCartProductRepository.findAllBySessionId(sessionId);
        for (RedisCartProduct product:products){
            List<RedisCartVariant> variantsAll = redisCartVariantRepository.findAllByProductCartId(product.getId());
            List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartIdAndActive(product.getId(),true);
            if (variantsAll.size()==variants.size()){
                redisCartProductRepository.delete(product);
            }
            if (variants!=null){
                redisCartVariantRepository.deleteAll(variants);
            }
        }
        return new CartRequestResponese(true,"delete success");
    }

    @Override
    public Boolean checkPayment() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        List<RedisCartProduct> products = redisCartProductRepository.findAllBySessionId(sessionId);
        for (RedisCartProduct redisCartProduct:products){
            List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartIdAndActive(redisCartProduct.getId(),true);
            if (variants.size()>0)return  true;
        }
        return false;
    }

}
