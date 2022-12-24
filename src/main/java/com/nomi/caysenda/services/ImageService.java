package com.nomi.caysenda.services;

import com.nomi.caysenda.entity.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface ImageService {
    Map upload(MultipartHttpServletRequest request,String type);
    Map uploadZip(MultipartHttpServletRequest request) throws FileNotFoundException;
    Page findAll(Pageable pageable);
    Page<ImageEntity> findAllByName(String keyword,Pageable pageable);
    Page findAllByType(String type,Pageable pageable);
    Page findAllByTypeAndName(String type,String name,Pageable pageable);
    Map deleteById(Integer id);
    void createZipThumbnail(Integer category) throws IOException;
    void createZipImages(Integer category) throws IOException;
    void updateImages(MultipartHttpServletRequest request) throws IOException;
    byte[] getInputImage(String url);
}
