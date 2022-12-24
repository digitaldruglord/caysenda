package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.entity.CategoryEntity;
import com.nomi.caysenda.services.CategoryService;
import com.nomi.caysenda.services.CronJob;
import com.nomi.caysenda.services.KeywordService;
import com.nomi.caysenda.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CronJobImpl implements CronJob {
    @Autowired
    ProductService productService;
    @Autowired
    KeywordService keywordService;
    @Scheduled(fixedRate = 180000)
    @Override
    public void scheduleUpdateCacheHomeProduct() {
        productService.removeproductFromCache();
        keywordService.reloadTopKeyword();
    }
}
