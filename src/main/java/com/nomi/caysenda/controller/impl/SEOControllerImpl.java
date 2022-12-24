package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.SEOController;
import com.nomi.caysenda.options.model.Robots;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SEOControllerImpl implements SEOController {
    @Autowired SettingService settingService;
    @Override
    public void robots(HttpServletRequest request, HttpServletResponse response) {
        Robots robots = settingService.getRobots();
        try {
            if (robots.getEnable()){
                response.getWriter().write(robots.getContent()!=null?robots.getContent():"");
            }else {

            }

        } catch (IOException e) {

        }
    }
}
