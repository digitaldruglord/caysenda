package com.nomi.caysenda.api.admin.impl;

import com.nomi.caysenda.api.admin.AdminDictionaryAPI;
import com.nomi.caysenda.entity.DictionaryEntity;
import com.nomi.caysenda.services.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dictionary")
public class AdminDictionaryAPIImpl implements AdminDictionaryAPI {
    @Autowired
    DictionaryService dictionaryService;
    @Override
    public ResponseEntity<Map> getDictionaries(Integer page, Integer pageSize, String zhWord) {
        Map map = new HashMap();
        map.put("success",true);
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:20, Sort.by(Sort.Direction.DESC,"id"));
        if (zhWord!=null){
            map.put("data",dictionaryService.findAllByZhWordAndViWord(zhWord,pageable));
        }else {
            map.put("data",dictionaryService.findAll(pageable));
        }
        return ResponseEntity.ok(map);
    }


    @Override
    public ResponseEntity<Map> updateDictionary(Integer id, String zhWord, String viWord) {
        Map map = new HashMap();
        DictionaryEntity dictionaryEntity =dictionaryService.createAndupdate(id, zhWord, viWord);
        if (dictionaryEntity!=null){
            map.put("success",true);
            map.put("data",dictionaryEntity);
        }else {
            map.put("success",false);
        }
        return ResponseEntity.ok(map);
    }

    @Override
    public ResponseEntity<Map> deleteDictionary(Integer id) {
        dictionaryService.deleteById(id);
        return ResponseEntity.ok(Map.of("success",true));
    }

    @Override
    public ResponseEntity<Map> updateExcel(MultipartHttpServletRequest request) {
        dictionaryService.uploadExcel(request);
        return ResponseEntity.ok(Map.of("success",true));
    }
}
