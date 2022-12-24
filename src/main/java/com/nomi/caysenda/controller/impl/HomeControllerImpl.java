package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.api1688.Service1688;
import com.nomi.caysenda.api1688.model.Product1688;
import com.nomi.caysenda.controller.HomeController;
import com.nomi.caysenda.controller.requests.cart.CartDetailtRequest;
import com.nomi.caysenda.controller.requests.cart.CartRequest;
import com.nomi.caysenda.dto.CategoryDTO;
import com.nomi.caysenda.entity.OrderDetailtEntity;
import com.nomi.caysenda.entity.OrderEntity;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.entity.VariantEntity;
import com.nomi.caysenda.exceptions.cart.AddToCartException;
import com.nomi.caysenda.ghn.entity.GHNOrderEntity;
import com.nomi.caysenda.ghn.repositories.GhnOrderRepository;
import com.nomi.caysenda.ghn.service.GHNService;
import com.nomi.caysenda.options.OptionKeys;
import com.nomi.caysenda.options.model.EmailSetting;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.*;
import com.nomi.caysenda.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Controller
@RequestMapping("")
public class HomeControllerImpl implements HomeController {
    @Autowired
    @Qualifier("productService")
    ProductService productService;
    @Autowired
    SettingService settingService;
    @Autowired
    GhnOrderRepository ghnOrderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public ModelAndView homeView(HttpServletRequest request, HttpServletResponse response) throws AddToCartException {
        ModelAndView view = new ModelAndView("index");
        SecurityContextHolder.getContext();
        view.addObject("slideHome",settingService.findAllSlideHome());
        view.addObject("bannerTop",settingService.findAllBannerTop());
        view.addObject("banner",settingService.findAllBanner());
        view.addObject("products",productService.getProductFromCache(0,20));

        return view;
    }

    @Override
    public ResponseEntity<InputStreamResource> exportTrack(List<Integer> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=Xuat-file-van-chuyen.xlsx");
        byte[] bytes = orderService.generateExcelTrack(ids);
        InputStream inputStream = new ByteArrayInputStream(bytes);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

    @Override
    public String test() {
       List<OrderEntity> orders = orderRepository.findAll();
        List<String> cashFlow = List.of("pendding","partiallypaid","paid");
       for (OrderEntity orderEntity:orders){
           if (cashFlow.contains(orderEntity.getStatus())){
               orderEntity.setCashflowstatus(orderEntity.getStatus());
           }
           orderRepository.save(orderEntity);
       }
        return "null";
    }


}
