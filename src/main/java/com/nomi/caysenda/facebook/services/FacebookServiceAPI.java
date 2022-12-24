package com.nomi.caysenda.facebook.services;

import com.nomi.caysenda.facebook.models.CreatePostRequest;
import com.nomi.caysenda.facebook.models.CreatePostResponse;
import com.nomi.caysenda.facebook.models.LongLiveAccessTokenRequest;
import com.nomi.caysenda.facebook.models.LongLiveAccessTokenResponse;

import java.util.List;

public interface FacebookServiceAPI {
    LongLiveAccessTokenResponse longLiveAccessToken(LongLiveAccessTokenRequest request);
    CreatePostResponse createPost(CreatePostRequest createPostRequest,String id,String accessToken);
    public String urlBuilder(Object obj);
    List<String> uploadImages(List<String> urls,String id,String accessToken);

}
