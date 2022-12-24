package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.dto.KeywordStatictisDTO;
import com.nomi.caysenda.entity.KeywordEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.repositories.KeywordRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.KeywordService;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class KeywordServiceImpl implements KeywordService {
    @Autowired KeywordRepository keywordRepository;
    @Autowired Environment env;
    @Autowired ProviderRepository providerRepository;
    @Override
    public List<String> findAllByKeyword(String keyword) {
        String domain = env.getProperty("spring.domain");
        return keywordRepository.searchAllByKey(keyword,domain);
    }

    @Override
    public List<String> findTopKeyword() {
        return findAllByKeyword("");
    }

    @Override
    public void reloadTopKeyword() {

    }

    @Override
    public void save(String keyword) {
        String domain = env.getProperty("spring.domain");
        ProviderEntity providerEntity = providerRepository.findByHost(domain);
        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setKeyword(keyword.trim().toLowerCase());
        keywordEntity.setProviderKeyword(List.of(providerEntity));
        keywordRepository.save(keywordEntity);

    }

    @Override
    public Page<KeywordStatictisDTO> statictis(Integer month, Integer year, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page!=null?page:0,pageSize!=null?pageSize:10);
        if (month==null && year == null) return keywordRepository.statictis(pageable);
        LocalDateTime now = LocalDateTime.now();
        if (month==null) month = now.getMonth().getValue();
        if (year==null) year  = now.getYear();
        Map<String, Date> dateMap = SpringUtils.getDateBetween("month"+month,year);
        return keywordRepository.statictis(dateMap.get("from"),dateMap.get("to"), pageable);
    }

}
