package com.nomi.caysenda.lazada.services.impl;

import com.google.gson.Gson;
import com.nomi.caysenda.lazada.api.LazopClient;
import com.nomi.caysenda.lazada.api.LazopRequest;
import com.nomi.caysenda.lazada.api.LazopResponse;
import com.nomi.caysenda.lazada.constants.LazadaContant;
import com.nomi.caysenda.lazada.services.LazadaSystemService;
import com.nomi.caysenda.lazada.services.model.LazadaGenerateToken;
import com.nomi.caysenda.lazada.util.ApiException;
import com.nomi.caysenda.options.model.LazadaSetting;
import com.nomi.caysenda.services.OptionService;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LazadaSystemServiceImpl implements LazadaSystemService {
    @Autowired
    OptionService optionService;
    @Autowired
    SettingService settingService;
    @Override
    public LazadaGenerateToken generate_accessToken(String code) throws ApiException {
        LazopClient client = new LazopClient(LazadaContant.URL, LazadaContant.APP_KEY, LazadaContant.APP_SECRECT);
        LazopRequest request = new LazopRequest();
        request.setApiName("/auth/token/create");
        request.addApiParameter("code", code);
        LazopResponse response = client.execute(request);
        return new Gson().fromJson(response.getBody(), LazadaGenerateToken.class);
    }

    @Override
    public Map getToken() {
        LazadaSetting lazadaSetting = settingService.getLazadaSetting();

        return lazadaSetting.getTokens()!=null && lazadaSetting.getTokens().size()>0?lazadaSetting.getTokens().get(0) : null;
    }
}
