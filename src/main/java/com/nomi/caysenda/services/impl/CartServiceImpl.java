package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.api.admin.model.order.request.AdminOrderSplitRequest;
import com.nomi.caysenda.controller.requests.cart.CartDetailtRequest;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.controller.responses.cart.CartRequestResponese;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.dto.cart.*;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.options.model.ShipSetting;
import com.nomi.caysenda.redis.model.RedisCartProduct;
import com.nomi.caysenda.redis.model.RedisCartVariant;
import com.nomi.caysenda.redis.repositories.RedisCartProductRepository;
import com.nomi.caysenda.redis.repositories.RedisCartVariantRepository;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.ProductUtils;
import com.nomi.caysenda.utils.model.ProductRangeAndRetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired CartRepository cartRepository;
    @Autowired UserRepository userRepository;
    @Autowired ProductRepository productRepository;
    @Autowired VariantRepository variantRepository;
    @Autowired CartProductRepository cartProductRepository;
    @Autowired CartVariantRepository cartVariantRepository;
    @Autowired CartRedisService cartRedisService;
    @Autowired CategoryRepository categoryRepository;
    @Autowired AddressProvinceRepository provinceRepository;
    @Autowired AddressDictrictRepository dictrictRepository;
    @Autowired AddressWardRepository wardRepository;
    @Autowired AddressRepository addressRepository;
    @Autowired AddressRedisService addressRedisService;
    @Autowired GiaoHangTietKiemService giaoHangTietKiemService;
    @Autowired SettingService settingService;
    @Autowired RedisCartProductRepository redisCartProductRepository;
    @Autowired RedisCartVariantRepository redisCartVariantRepository;
    @Autowired Environment env;
    @Autowired ProviderRepository providerRepository;
    @Autowired OrderService orderService;
    @Override
    public CartRequestResponese addtocart(CartRequest request) throws AddToCartException {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        PriceOption priceOption = settingService.getPriceSetting();
        if (priceOption.getLogin()){
            if (userDetail==null) throw new AddToCartException(AddToCartException.LOGIN_MESSAGE,AddToCartException.LOGIN);
        }
        if (userDetail!=null){
            CartEntity cartEntity = cartRepository.findByUserCart_IdAndDomain(userDetail.getUserId(),env.getProperty("spring.domain"));
            if (cartEntity!=null){
                CartProductEntity cartProductEntity = cartProductRepository.findByProductCartEntity_IdAndCartProduct_Id(request.getProductId(),cartEntity.getId());
                if (cartProductEntity!=null){
                    for (CartDetailtRequest detailtRequest:request.getDetailts()){
                        CartVariantEntity cartVariantEntity = cartVariantRepository.findByProduct_IdAndAndCartVariantEntity_Id(cartProductEntity.getId(),detailtRequest.getVariantId());
                        VariantEntity variantEntity = variantRepository.findById(detailtRequest.getVariantId()).get();
                        if (cartVariantEntity!=null){
                            if (variantEntity.getStock()<cartVariantEntity.getQuantity()+detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                            cartVariantEntity.setQuantity(cartVariantEntity.getQuantity()+detailtRequest.getQuantity());

                        }else {
                            if (variantEntity.getStock()<detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                            cartVariantEntity = new CartVariantEntity();
                            cartVariantEntity.setProduct(cartProductEntity);
                            cartVariantEntity.setQuantity(detailtRequest.getQuantity());
                            cartVariantEntity.setCartVariantEntity(variantEntity);
                            cartVariantEntity.setActive(false);
                            ProductEntity productEntity = productRepository.findById(request.getProductId()).get();
                            /** group */
                            GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                            if (groupVariantEntity!=null){
                                cartVariantEntity.setGroupName(groupVariantEntity.getName());
                                cartVariantEntity.setGroupZhName(groupVariantEntity.getZhName());
                                cartVariantEntity.setGroupSku(groupVariantEntity.getSkuGroup());
                            }
                            /** end group */
                        }
                        cartVariantRepository.save(cartVariantEntity);
                    }
                }else {
                    ProductEntity productEntity = productRepository.findById(request.getProductId()).get();
                    /**Check condition*/
                    Long total = request.getDetailts().stream().mapToLong(value -> value.getQuantity()).sum();
                    checkCondition(total,productEntity.getConditiondefault());
                    /** end check condition*/
                    cartProductEntity = new CartProductEntity();
                    cartProductEntity.setCartProduct(cartEntity);
                    cartProductEntity.setProductCartEntity(productEntity);
                    cartProductEntity.setRetailt(ProductUtils.isRetailt(productEntity,priceOption));
                    List<CartVariantEntity> variants = new ArrayList<>();
                    for (CartDetailtRequest detailtRequest:request.getDetailts()){
                        VariantEntity variantEntity = variantRepository.findById(detailtRequest.getVariantId()).get();
                        if (variantEntity.getStock()<detailtRequest.getQuantity()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
                        CartVariantEntity cartVariantEntity = new CartVariantEntity();
                        cartVariantEntity.setCartVariantEntity(variantEntity);
                        cartVariantEntity.setQuantity(detailtRequest.getQuantity());
                        cartVariantEntity.setProduct(cartProductEntity);
                        cartVariantEntity.setActive(false);
                        /** group */
                        GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                        if (groupVariantEntity!=null){
                            cartVariantEntity.setGroupName(groupVariantEntity.getName());
                            cartVariantEntity.setGroupZhName(groupVariantEntity.getZhName());
                            cartVariantEntity.setGroupSku(groupVariantEntity.getSkuGroup());
                        }
                        /** end group */
                        variants.add(cartVariantEntity);

                    }
                    cartProductEntity.setVariants(variants);
                }
                updateModified(cartEntity.getId());
                cartProductRepository.save(cartProductEntity);
            }else {
                cartEntity = new CartEntity();
                ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
                cartEntity.setCartProvider(List.of(providerEntity));
                ProductEntity productEntity = productRepository.findById(request.getProductId()).get();
                /**Check condition*/
                Long total = request.getDetailts().stream().mapToLong(value -> value.getQuantity()).sum();
                checkCondition(total,productEntity.getConditiondefault());
                /** end check condition*/
                UserEntity userEntity = userRepository.findById(userDetail.getUserId()).get();
                cartEntity.setUserCart(userEntity);
                CartProductEntity cartProductEntity = new CartProductEntity();
                cartProductEntity.setCartProduct(cartEntity);
                cartProductEntity.setProductCartEntity(productEntity);
                cartProductEntity.setRetailt(ProductUtils.isRetailt(productEntity,priceOption));
                List<CartVariantEntity> cartVariantEntities = new ArrayList<>();
                for (CartDetailtRequest detailtRequest:request.getDetailts()){
                    VariantEntity variantEntity = variantRepository.findById(detailtRequest.getVariantId()).get();
                    CartVariantEntity cartVariantEntity = new CartVariantEntity();
                    cartVariantEntity.setCartVariantEntity(variantEntity);
                    cartVariantEntity.setProduct(cartProductEntity);
                    cartVariantEntity.setQuantity(detailtRequest.getQuantity());
                    cartVariantEntity.setActive(false);
                    /** group */
                    GroupVariantEntity groupVariantEntity = ProductUtils.getGroupByGroupSku(productEntity,variantEntity.getParent());
                    if (groupVariantEntity!=null){
                        cartVariantEntity.setGroupName(groupVariantEntity.getName());
                        cartVariantEntity.setGroupZhName(groupVariantEntity.getZhName());
                        cartVariantEntity.setGroupSku(groupVariantEntity.getSkuGroup());
                    }
                    /** end group */
                    cartVariantEntities.add(cartVariantEntity);
                }
                cartProductEntity.setVariants(cartVariantEntities);
                cartEntity.setProducts(List.of(cartProductEntity));
                cartEntity.setModifiedDate(new Date());
                cartRepository.save(cartEntity);
            }
        }else {
            cartRedisService.addtocart(request);
        }
        return new CartRequestResponese(true,"add to cart success");
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
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        CartDTO cartDTO = new CartDTO();
        if (userDetail!=null){
            PriceOption priceOption = settingService.getPriceSetting();
            CartEntity cartEntity = cartRepository.findByUserCart_IdAndDomain(userDetail.getUserId(),env.getProperty("spring.domain"));
            if (cartEntity==null){
                cartEntity = new CartEntity();
                UserEntity userEntity = userRepository.findById(userDetail.getUserId()).orElse(null);
                cartEntity.setUserCart(userEntity);
            }
            List<CartProductDTO> products = cartProductRepository.findForCartDTO(cartEntity.getId());
            for (CartProductDTO productDTO:products){
                List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productDTO.getId());
                productDTO.setWeight(variants.stream().filter(cartVariantDTO ->
                        cartVariantDTO.getActive()).mapToDouble(cartVariantDTO ->
                        cartVariantDTO.getWeight()!=null && cartVariantDTO.getActive()?cartVariantDTO.getWeight()*cartVariantDTO.getQuantity():0).sum());
                productDTO.setVariants(variants);
                ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
                ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
            }

            List<CartCategoryDTO> categories = new ArrayList<>();
            products.stream().forEach((cartProductDTO) -> {
                List<CartCategoryDTO> list = categories.stream().filter(cartCategoryDTO -> cartCategoryDTO.getId().equals(cartProductDTO.getCategoryId())).collect(Collectors.toList());
                if (list == null || (list !=null && list.size() <= 0)) {
                    CartCategoryDTO cartCategoryDTO = new CartCategoryDTO();
                    cartCategoryDTO.setId(cartProductDTO.getCategoryId());
                    cartCategoryDTO.setProducts(new ArrayList<>(List.of(cartProductDTO)));
                    categories.add(cartCategoryDTO);
                } else {
                    list.get(0).getProducts().add(cartProductDTO);
                }
            });
            categories.stream().forEach(cartCategoryDTO -> {
                CategoryEntity categoryEntity = categoryRepository.findById(cartCategoryDTO.getId()).get();
                Boolean active = true;
                Long amount = Long.valueOf(0);
                for (CartProductDTO cartProductDTO:cartCategoryDTO.getProducts()){
                    if (!cartProductDTO.getActive()) active=false;
                    amount+=cartProductDTO.getAmount();
                }
                cartCategoryDTO.setName(categoryEntity.getName());
                cartCategoryDTO.setSlug(categoryEntity.getSlug());
                cartCategoryDTO.setCondition(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0);
                cartCategoryDTO.setActive(active);
                cartCategoryDTO.setAmount(amount);
            });

            cartDTO.setCategories(categories);
        }else {
            cartDTO = cartRedisService.getCart();
        }
        return cartDTO;
    }

    @Override
    public CartDTO getCart(Integer id) {
        CartDTO cartDTO = new CartDTO();
        PriceOption priceOption = settingService.getPriceSetting();
        CartEntity cartEntity = cartRepository.findById(id).orElse(null);
        List<CartProductDTO> products = cartProductRepository.findForCartDTO(cartEntity.getId());
        for (CartProductDTO productDTO:products){
            List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productDTO.getId());
            productDTO.setWeight(variants.stream().filter(cartVariantDTO ->
                    cartVariantDTO.getActive()).mapToDouble(cartVariantDTO ->
                    cartVariantDTO.getWeight()!=null && cartVariantDTO.getActive()?cartVariantDTO.getWeight()*cartVariantDTO.getQuantity():0).sum());
            productDTO.setVariants(variants);
            ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
            ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        }
        Map<Integer,List<CartProductDTO>> group = products.stream().collect(groupingBy(CartProductDTO::getCategoryId));
        List<CartCategoryDTO> categories = new ArrayList<>();
        group.entrySet().forEach((integerListEntry -> {
            CategoryEntity categoryEntity = categoryRepository.findById(integerListEntry.getKey()).get();
            Boolean active = true;
            Long amount = Long.valueOf(0);
            for (CartProductDTO cartProductDTO:integerListEntry.getValue()){
                if (!cartProductDTO.getActive()) active=false;
                amount+=cartProductDTO.getAmount();
            }
            CartCategoryDTO cartProductDTO = new CartCategoryDTO(integerListEntry.getKey(),categoryEntity.getName(),categoryEntity.getSlug(),(categoryEntity.getConditionPurchse()!=null?categoryEntity.getConditionPurchse():0),integerListEntry.getValue());
            cartProductDTO.setActive(active);
            cartProductDTO.setAmount(amount);
            categories.add(cartProductDTO);
        }));
        cartDTO.setCategories(categories);
        return cartDTO;
    }

    @Override
    public Map summaryCart(CartDTO cartDTO) {
        Long amountActive = Long.valueOf(0);
        Long totalQuantity = Long.valueOf(0);
        Long totalQuantityActive = Long.valueOf(0);
        Integer category = 0;
        Integer categoryActive = 0;
        Double weight = Double.valueOf(0);
        for (CartCategoryDTO cartCategoryDTO:cartDTO.getCategories()){
            category+=1;
            if (cartCategoryDTO.getActive()){
                categoryActive+=1;
            }
            for (CartProductDTO cartProductDTO:cartCategoryDTO.getProducts()){
                amountActive+=cartProductDTO.getAmountActive();
                totalQuantity+=cartProductDTO.getQuantity();
                totalQuantityActive+=cartProductDTO.getQuantityActive();
                weight+=cartProductDTO.getWeight();
            }
        }
        return Map.of(
                "amountActive",amountActive,
                "totalQuantity",totalQuantity,
                "totalQuantityActive",totalQuantityActive,
                "categoryQuantity",category,
                "categoryQuantityActive",categoryActive,
                "weight",weight
        );
    }

    @Override
    public Long countByProduct(Integer productId) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null){
            Long total = cartRepository.countByProductAndDomain(userDetail.getUserId(),productId,env.getProperty("spring.domain"));
            return total!=null?total:Long.valueOf(0);
        }else {
           return cartRedisService.countByProduct(productId);
        }
    }

    @Override
    public CartRequestResponese update(Long productId, Long variantId, Integer quantity) throws AddToCartException {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.update(productId,variantId,quantity);
        AtomicReference<Integer> totalQuantity = new AtomicReference<>(0);
        List<CartVariantDTO> variantEntities = cartVariantRepository.findAllByProductIdForCartVariantDTO(productId);
        variantEntities.forEach(variantDTO -> {
            if (!variantDTO.getId().equals(variantId)){
                totalQuantity.updateAndGet(v -> v + variantDTO.getQuantity());
            }
        });
        CartProductDTO cartProductDTO = cartProductRepository.findForCartDTOById(productId);
        CartVariantDTO cartVariantDTO = cartVariantRepository.findByIdForCartVariantDTO(variantId);
        PriceOption priceOption = settingService.getPriceSetting();
        if (cartVariantDTO!=null){
            if (totalQuantity.get()+quantity<cartProductDTO.getConditionDefault()) throw new AddToCartException(AddToCartException.ENOUGH_CONDITION,AddToCartException.ENOUGH_CONDITION_MESSAGE,cartVariantDTO.getQuantity());
            VariantEntity variantEntity = variantRepository.findById(cartVariantDTO.getVariantId()).get();
            if (quantity>variantEntity.getStock()) throw new AddToCartException(AddToCartException.NOT_ENOUGH_STOCK_MESSAGE,AddToCartException.NOT_ENOUGH_STOCK_CODE);
            CartVariantEntity cartVariant = cartVariantRepository.findById(cartVariantDTO.getId()).orElse(null);
            cartVariant.setQuantity(quantity);
            cartVariantRepository.save(cartVariant);
        }
        CartProductDTO productDTO = cartProductRepository.findForCartDTOById(productId);
        List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productId);
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(variantDTO -> variantDTO.getWeight()!=null?variantDTO.getWeight():0).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);

        /** check active product*/
        CategoryEntity categoryEntity = categoryRepository.findById(cartProductDTO.getCategoryId()).orElse(null);
        List<CartProductDTO> products = cartProductRepository.findForCartDTO(userDetail.getUserId(),categoryEntity.getId());
        Long amountCategory = productDTO.getAmount();
        for (CartProductDTO cartProductDTO1:products){
            if (!cartProductDTO1.getId().equals(productId)){
                List<CartVariantDTO> variantDTOS = cartVariantRepository.findAllByProductIdForCartVariantDTO(cartProductDTO1.getId());
                cartProductDTO1.setVariants(variantDTOS);
                cartProductDTO1.setWeight(variantDTOS.stream().mapToDouble(variantDTO -> variantDTO.getWeight()!=null?variantDTO.getWeight():0).sum());
                ProductRangeAndRetail productRangeAndRetail1 = ProductUtils.isRetailt(cartProductDTO1,priceOption);
                ProductUtils.getFullDataCartProductDTO(cartProductDTO1,productRangeAndRetail1,priceOption);
                amountCategory+=cartProductDTO1.getAmountActive();
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
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        PriceOption priceOption = settingService.getPriceSetting();
        if (userDetail==null) return cartRedisService.activeAll(active);
        CartDTO cartDTO = getCart();
        for (CartCategoryDTO cartCategoryDTO:cartDTO.getCategories()){
            if (active){
                Long amount = cartCategoryDTO.getProducts().stream().mapToLong((cartProductDTO -> cartProductDTO.getAmount())).sum();
                Boolean check = true;
                if (!priceOption.getEnableConditionCategory()){
                    check=amount>=(cartCategoryDTO.getCondition()!=null?cartCategoryDTO.getCondition():0);
                }
                List<CartVariantEntity> variants = cartVariantRepository.findByCatId(cartCategoryDTO.getId());
                for (CartVariantEntity variantEntity:variants){
                    variantEntity.setActive(check);
                }
                cartVariantRepository.saveAll(variants);
            }else {
                List<CartVariantEntity> variants = cartVariantRepository.findByCatId(cartCategoryDTO.getId());
                for (CartVariantEntity variantEntity:variants){
                    variantEntity.setActive(active);
                }
                cartVariantRepository.saveAll(variants);
            }


        }
       return new CartRequestResponese(true,"active success");
    }

    @Override
    public CartRequestResponese activeByCat(Integer cat,Boolean active) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.activeByCat(cat,active);
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        List<CartProductDTO> products = cartProductRepository.findForCartDTOByCat(cat,userDetail.getUserId());
        CategoryEntity categoryEntity = categoryRepository.findById(cat).orElse(null);
        /***/
        /** item update*/
        AtomicReference<Long> amountCategory = new AtomicReference<>(Long.valueOf(0));
        products.forEach(productDTO -> {
            List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productDTO.getId());
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
                CartVariantEntity variantEntity = cartVariantRepository.findById(variantDTO.getId()).orElse(null);
                if (variantEntity!=null){
                    variantEntity.setActive(finalCheck);
                    cartVariantRepository.save(variantEntity);
                }

            });
        });
        if (check){
            return new CartRequestResponese(true,"active true");
        }else {
            return new CartRequestResponese(false,"active true");
        }
    }

    @Override
    public CartRequestResponese activeByProduct(Integer catId,Long product,Boolean active) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.activeByProduct(catId,product,active);
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        CategoryEntity categoryEntity = categoryRepository.findById(catId).orElse(null);
        List<CartProductDTO> products = cartProductRepository.findForCartDTOByCat(catId,userDetail.getUserId());
            /***/
        /** item update*/

        CartProductDTO productDTO = cartProductRepository.findForCartDTOById(product);
        List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productDTO.getId());
        variants.forEach(variantDTO -> {
            variantDTO.setActive(active);
            CartVariantEntity cartVariant = cartVariantRepository.findById(variantDTO.getId()).orElse(null);
            cartVariant.setActive(active);
            cartVariantRepository.save(cartVariant);
        });
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight():0).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        if (!priceOption.getEnableConditionCategory()){
            /** check condition*/
            Long amountCategory = productDTO.getAmountActive();
            for (CartProductDTO cartProductDTO:products){
                if (!cartProductDTO.getId().equals(product)){
                    List<CartVariantDTO> variantDTOS = cartVariantRepository.findAllByProductIdForCartVariantDTO(cartProductDTO.getId());
                    cartProductDTO.setVariants(variantDTOS);
                    productDTO.setWeight(variantDTOS.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight():0).sum());
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
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.activeByVariant(catId,product,variant,active);
        PriceOption priceOption = settingService.getPriceSetting();
        /***/
        CategoryEntity categoryEntity = categoryRepository.findById(catId).orElse(null);
        List<CartProductDTO> products = cartProductRepository.findForCartDTOByCat(catId,userDetail.getUserId());
        /***/
        /** item update*/
        CartProductDTO productDTO = cartProductRepository.findForCartDTOById(product);
        List<CartVariantDTO> variants = cartVariantRepository.findAllByProductIdForCartVariantDTO(productDTO.getId());
        variants.forEach(variantDTO -> {
            if (variantDTO.getId().equals(variant)){
                variantDTO.setActive(active);
                CartVariantEntity cartVariant = cartVariantRepository.findById(variantDTO.getId()).orElse(null);
                cartVariant.setActive(active);
                cartVariantRepository.save(cartVariant);
            }

        });
        productDTO.setVariants(variants);
        productDTO.setWeight(variants.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight():0).sum());
        ProductRangeAndRetail productRangeAndRetail = ProductUtils.isRetailt(productDTO,priceOption);
        ProductUtils.getFullDataCartProductDTO(productDTO,productRangeAndRetail,priceOption);
        if (!priceOption.getEnableConditionCategory()){
            /** check condition*/
            Long amountCategory = productDTO.getAmountActive();
            for (CartProductDTO cartProductDTO:products){
                if (!cartProductDTO.getId().equals(product)){
                    List<CartVariantDTO> variantDTOS = cartVariantRepository.findAllByProductIdForCartVariantDTO(cartProductDTO.getId());
                    cartProductDTO.setVariants(variantDTOS);
                    cartProductDTO.setWeight(variantDTOS.stream().mapToDouble(cartVariantDTO -> cartVariantDTO.getWeight()!=null?cartVariantDTO.getWeight():0).sum());
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
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.removeByProduct(product);
        cartProductRepository.deleteById(product);
        return new CartRequestResponese(true,"remove success");
    }

    @Override
    public CartRequestResponese removeByProduct(List<Long> products) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) {
            for (Long product : products) {
                cartRedisService.removeByProduct(product);
            }

            return new CartRequestResponese(true,"remove success");
        }

        for (Long product : products) {
            cartProductRepository.deleteById(product);
        }

        return new CartRequestResponese(true,"remove success");
    }

    @Override

    public CartRequestResponese removeAllActive() {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null){
            cartVariantRepository.deleteByActiveAndProduct_CartProduct_UserCart_Id(true,userDetail.getUserId());
            cartProductRepository.deleteAll(cartProductRepository.findAllByVariantsLessThanZero());
        }else {
            cartRedisService.removeAllActive();
        }

        return new CartRequestResponese(true,"remove success");
    }

    @Override
    public Map getDataWidget() {
        Map map = new HashMap();
        AtomicReference<Long> amount = new AtomicReference<>(Long.valueOf(0));
        AtomicReference<Integer> count = new AtomicReference<>(0);
        CartDTO cartDTO = getCart();
        cartDTO.getCategories().forEach(cartCategoryDTO -> {
            cartCategoryDTO.getProducts().forEach(productDTO -> {
                amount.updateAndGet(v -> v + productDTO.getAmount());
                productDTO.getVariants().forEach(cartVariantDTO -> {
                    count.updateAndGet(v -> v + cartVariantDTO.getQuantity());
                });
            });
        });
        map.put("amount",amount.get());
        map.put("count",count.get());
        return map;
    }

    @Override
    public Map getDelivery() {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        AddressDTO addressDTO = null;
        CartDTO cartDTO = getCart();
        cartDTO.getCategories().forEach(cartCategoryDTO -> cartCategoryDTO.getProducts().forEach(productDTO -> {
            productDTO.getWeight();
        }));
        Map map = new HashMap();
        map.putAll(summaryCart(cartDTO));
        ShipSetting shipSetting = settingService.getShipseting();
        Long fee = Long.valueOf(0);
        Double weightG = (Double) map.get("weight"); // g
        Double weightKg = weightG/1000;// kg
        if (weightKg<=5){
            fee = shipSetting.getFee();
        }else {
            Double extraFee = (weightKg-5)*shipSetting.getExtraFee();
            fee = shipSetting.getFee()+extraFee.longValue();
        }
        map.put("fee",fee);
//        if (userDetail!=null){
//            addressDTO = addressRepository.findByDefaultAddress(userDetail.getUserId());
//        }else {
//            addressDTO = addressRedisService.findAddressDefault();
//        }
//        if (addressDTO!=null){
//            AddressProviceEntity proviceEntity = provinceRepository.findById(addressDTO.getProvince()).orElse(null);
//            AddressDictrictEntity dictrictEntity = dictrictRepository.findById(addressDTO.getDictrict()).orElse(null);
//            map.put("shipsetting",shipSetting);
//            map.putAll(giaoHangTietKiemService.getFeeShip(proviceEntity.getName(),dictrictEntity.getName(),addressDTO.getAddress(), String.valueOf(map.get("weight"))));
//        }else {
//            map.put("fee",0);
//        }
        return map;
    }

    @Override
    public void login(String sessionId) {
         List<RedisCartProduct> products = redisCartProductRepository.findAllBySessionId(sessionId);
         products.forEach(redisCartProduct -> {
             CartRequest request = new CartRequest();
             request.setProductId(redisCartProduct.getProductId());
             List<CartDetailtRequest> detailts = new ArrayList<>();
             List<RedisCartVariant> variants = redisCartVariantRepository.findAllByProductCartId(redisCartProduct.getId());
             variants.forEach(redisCartVariant -> {
                 detailts.add(new CartDetailtRequest(redisCartVariant.getVariantId(),redisCartVariant.getQuantity()));
             });
             request.setDetailts(detailts);
             try {
                 addtocart(request);
             } catch (AddToCartException e) {

             }
         });
    }

    @Override
    public Boolean checkPayment() {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail==null) return cartRedisService.checkPayment();
        List<CartVariantEntity> variants = cartVariantRepository.findAllByUserId(userDetail.getUserId());
        for (CartVariantEntity variantEntity:variants){
            if (variantEntity.getActive()){
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<CartSummary> statictisCart(String sort, String keyword, Pageable pageable) {

        Page<CartSummary> ids = cartRepository.statictisCart(sort,keyword , pageable);
        ids.getContent().forEach(summary -> {
            List<AddressEntity> addressEntities = addressRepository.findAllByUserAddress_Id(summary.getUserId());
            List<String> address = new ArrayList<>();
            for (AddressEntity addressEntity:addressEntities){
                address.add(addressEntity.getPhoneNumber()+" - "+orderService.getFullAddress(addressEntity.getAddress(),addressEntity.getProvince(),addressEntity.getDictrict(),addressEntity.getWard()));
            }
            summary.setAddress(address);
            CartDTO cartDTO = getCart(summary.getId());
            Long amount = Long.valueOf(0);
            Long cost = Long.valueOf(0);
            for (CartCategoryDTO categoryDTO:cartDTO.getCategories()){
                amount+=categoryDTO.getAmount();
                for (CartProductDTO productDTO:categoryDTO.getProducts()){
                    for (CartVariantDTO variantDTO:productDTO.getVariants()){
                        cost+=variantDTO.getCost()*variantDTO.getQuantity();
                    }
                }
            }
            summary.setAmount(amount);
            summary.setCost(cost);
            summary.setProfit(amount-cost);

        });
        return ids;
    }

    @Override
    public CartSummary statictisCart(Integer id) {
        CartSummary summary = cartRepository.findAllOrderById(id);
        List<AddressEntity> addressEntities = addressRepository.findAllByUserAddress_Id(summary.getUserId());
        List<String> address = new ArrayList<>();
        for (AddressEntity addressEntity:addressEntities){
            address.add(addressEntity.getPhoneNumber()+" - "+orderService.getFullAddress(addressEntity.getAddress(),addressEntity.getProvince(),addressEntity.getDictrict(),addressEntity.getWard()));
        }
        summary.setAddress(address);

        CartDTO cartDTO = getCart(id);
        Long amount = Long.valueOf(0);
        Long cost = Long.valueOf(0);
        for (CartCategoryDTO categoryDTO:cartDTO.getCategories()){
            amount+=categoryDTO.getAmount();
            for (CartProductDTO productDTO:categoryDTO.getProducts()){
                for (CartVariantDTO variantDTO:productDTO.getVariants()){
                    cost+=variantDTO.getCost()*variantDTO.getQuantity();
                }
            }
        }
        summary.setAmount(amount);
        summary.setCost(cost);
        summary.setProfit(amount-cost);
        return summary;
    }

    @Override
    public void updateCartNote(Integer id, String note) {
        CartEntity cartEntity = cartRepository.findById(id).orElse(null);
        if (cartEntity!=null){
            cartEntity.setNote(note);
            cartRepository.save(cartEntity);
        }
    }

    @Override
    public void updateModified(Integer id) {
        CartEntity cartEntity = cartRepository.findById(id).orElse(null);
        if (cartEntity!=null){
            cartEntity.setModifiedDate(new Date());
            cartRepository.save(cartEntity);
        }
    }
}
