package com.nomi.caysenda.lazada.services;



import com.nomi.caysenda.lazada.services.model.LazadaGenerateToken;
import com.nomi.caysenda.lazada.util.ApiException;

import java.util.Map;

public interface LazadaSystemService {
   LazadaGenerateToken generate_accessToken(String code) throws ApiException;
   Map getToken();
}
