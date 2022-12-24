package com.nomi.caysenda.dialect.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlBuilderUtilsDialect {
    public UrlBuilderUtilsDialect() {
    }

    public String builder(String baseUrl,Map<String,String[]> params,String ...data){
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append("?");
        Map map = new HashMap();
        params.entrySet().stream().forEach(stringEntry ->{
            String key = stringEntry.getKey();
            String[] values = stringEntry.getValue();
            Arrays.stream(values).forEach(s -> {
                map.put(key,s);
            });
        });
        for (int i=0;i<data.length;i=i+2){
            map.put(data[i],data[i+1]);
        }
        String mapAsString = (String) map.keySet().stream().map(key -> key + "=" + String.valueOf(map.get(key))).collect(Collectors.joining("&"));
        builder.append(mapAsString);
        return builder.toString();
    }
}
