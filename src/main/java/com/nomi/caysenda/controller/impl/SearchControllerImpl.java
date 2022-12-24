package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.SearchController;
import com.nomi.caysenda.services.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/ajax/search")
public class SearchControllerImpl implements SearchController {
    @Autowired
    KeywordService keywordService;
    @Override
    public ResponseEntity<Map> search(String keyword) {
        List<String> data = keywordService.findAllByKeyword(keyword);
        return ResponseEntity.ok(Map.of("success",true,"data",data));
    }
}
