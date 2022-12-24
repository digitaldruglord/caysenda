package com.nomi.caysenda.facebook.services.impl;

import com.nomi.caysenda.facebook.FacebookConstant;
import com.nomi.caysenda.facebook.models.*;
import com.nomi.caysenda.facebook.services.FacebookServiceAPI;
import com.nomi.caysenda.options.model.FacebookSetting;
import com.nomi.caysenda.services.SettingService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacebookServiceAPIImpl implements FacebookServiceAPI {
    @Autowired RestTemplate restTemplate;
    @Autowired SettingService settingService;
    @Autowired Environment environment;
    @Override
    public LongLiveAccessTokenResponse longLiveAccessToken(LongLiveAccessTokenRequest request) {
        FacebookSetting facebookSetting = settingService.getFacebook();
        request.setClient_id(facebookSetting.getAppId());
        request.setClient_secret(facebookSetting.getAppSecrect());
        request.setGrant_type("fb_exchange_token");
        HttpEntity entity = new HttpEntity<>(null);
        ResponseEntity<LongLiveAccessTokenResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(FacebookConstant.LONGLIVEACCESSTOKEN+"?"+urlBuilder(request),HttpMethod.GET,entity,LongLiveAccessTokenResponse.class);
        }catch (HttpClientErrorException httpClientErrorException){
            httpClientErrorException.getResponseBodyAsString();
        }
        if (responseEntity==null) return null;
        return responseEntity.getBody();
    }

    @Override
    public CreatePostResponse createPost(CreatePostRequest createPostRequest,String id,String accessToken) {
        HttpEntity<CreatePostRequest> entity = new HttpEntity<>(createPostRequest);
        ResponseEntity<CreatePostResponse> responseEntity = null;
        try{
            responseEntity = restTemplate.exchange(
                    "https://graph.facebook.com/"+id+"/feed?access_token="+accessToken,
                    HttpMethod.POST,
                    entity,
                    CreatePostResponse.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException httpClientErrorException){
            httpClientErrorException.getResponseBodyAsString();
            return null;
        }

    }

    public String urlBuilder(Object obj){
        List<String> strings = Arrays.stream(obj.getClass().getDeclaredFields()).filter(field -> {
            field.setAccessible(true);
            try {
               return  field.get(obj)!=null;
            } catch (IllegalAccessException e) {
                return false;
            }
        }).map(field -> {
            field.setAccessible(true);
            try {
                return field.getName()+"="+field.get(obj);
            } catch (IllegalAccessException e) {
                return "";
            }
        }).collect(Collectors.toList());

        return strings.stream().collect(Collectors.joining("&"));
    }

    @Override
    public List<String> uploadImages(List<String> urls, String id, String accessToken) {
        List<String> ids = new ArrayList<>();

        UrlValidator urlValidator = new UrlValidator();
        urls.forEach(s -> {
            UploadImageRequest modelRequest = new UploadImageRequest(urlValidator.isValid(s)?s:"https://caysenda.vn"+s,false);

            HttpEntity<UploadImageRequest> entity = new HttpEntity<>(modelRequest);
            ResponseEntity<UploadImageResponse> responseEntity = null;
            try{
                responseEntity = restTemplate.exchange(
                        "https://graph.facebook.com/"+id+"/photos?access_token="+accessToken,
                        HttpMethod.POST,
                        entity,
                        UploadImageResponse.class
                );
                ids.add(responseEntity.getBody().getId());
            } catch (HttpClientErrorException httpClientErrorException){
                System.err.println(httpClientErrorException.getResponseBodyAsString());

            }


        });
        return ids;
    }
    private FacebookSetting getDataSetting(){
        return settingService.getFacebook();
    }

}
