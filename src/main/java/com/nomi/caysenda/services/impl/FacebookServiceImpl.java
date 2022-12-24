package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.dialect.utils.ProductUtilsDialect;
import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.facebook.entity.FacebookEntity;
import com.nomi.caysenda.facebook.models.AttachedMedia;
import com.nomi.caysenda.facebook.models.CreatePostRequest;
import com.nomi.caysenda.facebook.models.CreatePostResponse;
import com.nomi.caysenda.facebook.repositories.FacebookRepository;
import com.nomi.caysenda.facebook.services.FacebookServiceAPI;
import com.nomi.caysenda.options.model.FacebookSetting;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.redis.model.RedisRunning;
import com.nomi.caysenda.redis.services.RedisRunningService;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class FacebookServiceImpl implements FacebookService {
    @Autowired FacebookServiceAPI facebookServiceAPI;
    @Autowired CategoryService categoryService;
    @Autowired ProductService productService;
    @Autowired SettingService settingService;
    @Autowired Environment env;
    @Autowired ProgressService progressService;
    @Autowired FacebookRepository facebookRepository;
    @Autowired TaskScheduler taskScheduler;
    @Autowired RedisRunningService redisRunningService;
    final String FACEBOOK_PROGESS = "FACEBOOK_PROGESS";
    final String FACEBOOK_RUNNING= "FACEBOOK_RUNNING";
    @Override
    public Page<FacebookEntity> findAllUploaded(Pageable pageable) {
        return facebookRepository.findAllByHost(env.getProperty("spring.domain"),pageable);
    }

    @Override
    public void createPostByCat(Integer catId) {
        CategoryEntity categoryEntity = categoryService.findById(catId).orElse(null);
        List<ProductEntity> products = productService.findAllByCatAndHost(catId, env.getProperty("spring.domain"));
        progressService.save(0, 100, FACEBOOK_PROGESS);
        redisRunningService.save(FACEBOOK_RUNNING);
        taskScheduler.schedule(() -> {
            AtomicReference<Integer> i = new AtomicReference<>(0);
            FacebookSetting facebookSetting = settingService.getFacebook();
            AtomicReference<RedisRunning> redisRunning = new AtomicReference<>(redisRunningService.findByTaskCode(FACEBOOK_RUNNING));
            products.forEach(productEntity -> {
                redisRunning.set(redisRunningService.findByTaskCode(FACEBOOK_RUNNING));
                if (!facebookRepository.existsByProductIdAndHost(productEntity.getId(),env.getProperty("spring.domain")) && redisRunning.get() !=null) {
                    List<String> images = facebookServiceAPI.uploadImages(productEntity.getGallery(), facebookSetting.getPageId(),facebookSetting.getPageAccessToken());
                    if (images.size()<=0){

                    }else {
                        CreatePostRequest request = new CreatePostRequest();
                        request.setAttached_media(images.stream().map(s -> {
                            return new AttachedMedia(s);
                        }).collect(Collectors.toList()));
                        request.setMessage(generateContent(productEntity, categoryEntity, facebookSetting.getContent()));
                        request.setPublished(true);
                        CreatePostResponse response = facebookServiceAPI.createPost(request,facebookSetting.getPageId(),facebookSetting.getPageAccessToken());
                        if (response != null) {
                            FacebookEntity facebookEntity = new FacebookEntity();
                            facebookEntity.setProductId(productEntity.getId());
                            facebookEntity.setProductName(productEntity.getNameVi());
                            facebookEntity.setCateId(categoryEntity.getId());
                            facebookEntity.setCatName(categoryEntity.getName());
                            facebookEntity.setPostId(response.getId());
                            facebookEntity.setHost(env.getProperty("spring.domain"));
                            facebookRepository.save(facebookEntity);
                        }
                    }

                }
                progressService.save(i.getAndSet(i.get() + 1), products.size(), FACEBOOK_PROGESS);
            });
            progressService.delete(FACEBOOK_PROGESS);
        }, new Date());

    }


    @Override
    public Long countUploaded(Integer catID) {
        Long count = facebookRepository.countByCateId(catID);

        return count!=null?count:0;
    }

    @Override
    public void deletePostByCat(Integer catId) {
        List<FacebookEntity> datas = facebookRepository.findAllByCateIdAndHost(catId,env.getProperty("spring.domain"));
        facebookRepository.deleteAll(datas);
    }

    @Override
    public void cancelRunning() {
        redisRunningService.delete(FACEBOOK_RUNNING);
        progressService.delete(FACEBOOK_PROGESS);
    }

    private String generateContent(ProductEntity productEntity, CategoryEntity categoryEntity, String template) {
        String domain = env.getProperty("spring.domain");
        PriceOption priceOption = settingService.getPriceSetting();
        ProductUtilsDialect productUtilsDialect = new ProductUtilsDialect();
        String min = productUtilsDialect.getPriceByType(priceOption.getPriceDefault(),productEntity,"min");
        String vip1 = productUtilsDialect.getPriceByType(priceOption.getPrice1(),productEntity);
        Boolean isRetailt = ProductUtils.isRetailt(productEntity,priceOption);
        template = template.replace("{productName}", productEntity.getNameVi())
                .replace("{productLink}", domain + "/" + categoryEntity.getSlug() + "/" + productEntity.getSlugVi())
                .replace("{catLink}", domain + "/" + categoryEntity.getSlug())
                .replace("{productDescription}", productEntity.getDescription() != null ? productEntity.getDescription() : "");
        if (isRetailt){
            template = template.replace("{price}",min!=null?min:"");
        }else {
            template = template.replace("{price}",vip1!=null?vip1:"");
        }
        return template;
    }

    ;
}
