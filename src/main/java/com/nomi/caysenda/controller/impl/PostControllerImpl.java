package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.PostController;
import com.nomi.caysenda.entity.PageEntity;
import com.nomi.caysenda.exceptions.PageNotFountException;
import com.nomi.caysenda.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/bai-viet")
public class PostControllerImpl implements PostController {
    @Autowired
    PageService pageService;

    @Override
    public ModelAndView posts(Integer page, Integer pageSize, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("post-pages/index");
        page = page!=null?page-1:0;
        pageSize = pageSize!=null?pageSize:20;
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<PageEntity> pages = pageService.findAll("post",pageable);
        view.addObject("data",pages);
        view.addObject("totalPages",pages.getTotalPages());
        view.addObject("params",request.getParameterMap());
        view.addObject("page",page+1);
        return view;
    }

    @Override
    public ModelAndView postDetailt(String slug) throws PageNotFountException {
        ModelAndView view = new ModelAndView("post-pages/detailt/index");
        PageEntity pageEntity = pageService.findBySlug(slug);
        if (pageEntity==null) throw new PageNotFountException();
        view.addObject("data",pageEntity);
        return view;
    }
}
