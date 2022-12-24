package com.nomi.caysenda.utils;

import org.springframework.web.context.request.RequestContextHolder;

public class SessionUtils {
    public static String getDomain(){
        return String.valueOf(RequestContextHolder.currentRequestAttributes().getAttribute("domain",0));
    }
}
