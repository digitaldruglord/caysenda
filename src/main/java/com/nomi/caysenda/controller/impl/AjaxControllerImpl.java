package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.AjaxController;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.dto.ProductGallery;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.ProductService;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@Controller
@RequestMapping("/ajax")
public class AjaxControllerImpl implements AjaxController {
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SettingService settingService;
    @Override
    public ResponseEntity<Map> ProductRandlazyLoad(String tab, Integer categoryId) {
        Map map = new HashMap();
        map.put("success",true);
        Context context = new Context();
        context.setVariable("priceSetting",settingService.getPriceSetting());
        switch (tab){
            case "new":
                context.setVariable("products",productService.findRandForProductDTO(PageRequest.of(0,20)).getContent());;
                map.put("fragment",templateEngine.process("lazy/rand-product",context));
                break;
            case "bestselling":
                context.setVariable("products",productService.findRandForProductDTO(PageRequest.of(0,20)).getContent());;
                map.put("fragment",templateEngine.process("lazy/rand-product",context));
                break;
            case "appreciate":
                context.setVariable("products",productService.findRandForProductDTO(PageRequest.of(0,20)).getContent());;
                map.put("fragment",templateEngine.process("lazy/rand-product",context));
                break;
            case "framecategory":
                List<CategoryDTO> categoryDTOS = categoryService.findAllByDomainForCategoryDTO();
                Random rand = new Random();
                Integer category = rand.nextInt(categoryDTOS.size());
                Collections.swap(categoryDTOS, 0, category);
                context.setVariable("products",productService.findByCategoryRandForProductDTO(categoryDTOS.get(0).getId(),PageRequest.of(0,12)));
                context.setVariable("categorylist",categoryDTOS);
                map.put("fragment",templateEngine.process("lazy/frame-category",context));
                break;
            case "framecategorytab":
                context.setVariable("products",productService.findByCategoryRandForProductDTO(categoryId,PageRequest.of(0,12)));
//                context.setVariable("productGallery",productService.findRandForProductGallery(PageRequest.of(0,1)).getContent().get(0));
                map.put("fragment",templateEngine.process("lazy/lazy-frame-category-tab-content",context));
                break;
            case "framebestseller":
                context.setVariable("products",productService.findRandForProductDTO(PageRequest.of(0,24)).getContent());
                map.put("fragment",templateEngine.process("lazy/bestsellers",context));
                break;
            case "footerproduct":
                context.setVariable("website",settingService.getWebsite());
                context.setVariable("products",productService.findRandForProductDTO(PageRequest.of(0,15)).getContent());
                map.put("fragment",templateEngine.process("lazy/footer/top-product",context));
                break;
            case "related":
                context.setVariable("products",productService.findByCategoryRandForProductDTO(categoryId,PageRequest.of(0,12)).getContent());
                map.put("fragment",templateEngine.process("lazy/related-product",context));
                break;
        }


        return ResponseEntity.ok(map);
    }
}
