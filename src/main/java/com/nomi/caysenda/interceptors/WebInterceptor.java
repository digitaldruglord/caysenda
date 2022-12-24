package com.nomi.caysenda.interceptors;

import com.nomi.caysenda.model.SEO;
import com.nomi.caysenda.options.OptionKeys;
import com.nomi.caysenda.options.model.BrandOption;
import com.nomi.caysenda.options.model.PriceOption;
import com.nomi.caysenda.options.model.WebsiteInfo;
import com.nomi.caysenda.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component("webInterceptor")
public class WebInterceptor implements HandlerInterceptor {
    @Autowired
    CategoryService categoryService;
    @Autowired
    @Qualifier("optionService")
    OptionService optionService;
    @Autowired
    SettingService settingService;
    @Autowired
    SEOService seoService;
    @Autowired
    KeywordService keywordService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /// Top keyword
        request.setAttribute("topkeys",keywordService.findTopKeyword());
        /** Nav top*/
        request.setAttribute("navTop",settingService.findAllMenu(OptionKeys.NAVTOP));
        /** Nav category*/
        request.setAttribute("navCategory",settingService.findAllMenu(OptionKeys.NAVCATEGORY));
        /** Nav Footer*/
        request.setAttribute("navFooter1",settingService.findAllMenu(OptionKeys.NAVFOOTER));
        request.setAttribute("navFooter2",settingService.findAllMenu(OptionKeys.NAVFOOTER1));
        request.setAttribute("navFooter3",settingService.findAllMenu(OptionKeys.NAVFOOTER2));
        /** Website */
        WebsiteInfo websiteInfo = settingService.getWebsite();
        websiteInfo =  websiteInfo!=null?websiteInfo:new WebsiteInfo();
        request.setAttribute("website",websiteInfo!=null?websiteInfo:new WebsiteInfo());
        /** Website */
        PriceOption priceOption = settingService.getPriceSetting();
        request.setAttribute("priceSetting",priceOption);
        /** Website */
        List<BrandOption> brands = settingService.getBrands();
        request.setAttribute("brands",brands);
        /** SEO */
        SEO seo = seoService.getSEO(request.getRequestURI(),websiteInfo);
        request.setAttribute("seo",seo);


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

}
