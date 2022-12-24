package com.nomi.caysenda.services.impl;

import com.nomi.caysenda.api.admin.model.post.AdminPostRequest;
import com.nomi.caysenda.entity.PageEntity;
import com.nomi.caysenda.entity.ProviderEntity;
import com.nomi.caysenda.repositories.PageRepository;
import com.nomi.caysenda.repositories.ProviderRepository;
import com.nomi.caysenda.services.PageService;
import com.nomi.caysenda.utils.ConvertStringToUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@Service
public class PageServiceImpl implements PageService {
    @Autowired PageRepository pageRepository;
    @Autowired Environment env;
    @Autowired ProviderRepository providerRepository;

    @Override
    public List<PageEntity> findAllByDomain(String domain) {
        return pageRepository.findAll(domain);

    }

    @Override
    public Page<PageEntity> findAll(Pageable pageable) {
        return pageRepository.findAll(env.getProperty("spring.domain"),pageable);
    }

    @Override
    public Page<PageEntity> findAll(String type, Pageable pageable) {
        return pageRepository.findAll(type,env.getProperty("spring.domain"),pageable);
    }

    @Override
    public PageEntity findById(Integer id) {
        return pageRepository.findByIdAndHost(id,env.getProperty("spring.domain"));
    }

    @Override
    public PageEntity findBySlug(String slug) {
        return pageRepository.findBySlugAndHost(slug,env.getProperty("spring.domain"));
    }

    @Override
    public PageEntity save(AdminPostRequest postRequest) {
        PageEntity pageEntity = new PageEntity();
        if (postRequest.getId()!=null) pageEntity.setId(postRequest.getId());
        if (postRequest.getSlug()!=null) pageEntity.setSlug(postRequest.getSlug());
        if (postRequest.getName()!=null) pageEntity.setName(postRequest.getName());
        if (postRequest.getName()!=null) pageEntity.setSlug(ConvertStringToUrl.covertStringToURL(postRequest.getName()));
        if (postRequest.getContent()!=null) pageEntity.setContent(postRequest.getContent());
        if (postRequest.getCss()!=null) pageEntity.setCss(postRequest.getCss());
        if (postRequest.getDescription()!=null) pageEntity.setDescription(postRequest.getDescription());
        if (postRequest.getImage()!=null) pageEntity.setImage(postRequest.getImage());
        if (postRequest.getType()!=null) pageEntity.setType(postRequest.getType());
        ProviderEntity providerEntity = providerRepository.findByHost(env.getProperty("spring.domain"));
        if (providerEntity!=null){
            pageEntity.setProviderPage(List.of(providerEntity));
        }
        return pageRepository.save(pageEntity);
    }

    @Override
    public void deleteById(Integer id) {
        pageRepository.deleteById(id);
    }

    @Override
    public Boolean existsBySlug(String slug) {
        return pageRepository.existsBySlugAndHost(slug,env.getProperty("spring.domain"));
    }
}
