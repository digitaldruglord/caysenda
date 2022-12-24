package com.nomi.caysenda.controller.impl;

import com.nomi.caysenda.controller.CartController;
import com.nomi.caysenda.dto.cart.CartDTO;
import com.nomi.caysenda.repositories.AddressRepository;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.services.CartService;
import com.nomi.caysenda.services.OrderService;
import com.nomi.caysenda.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;


@Controller
@RequestMapping("/gio-hang")
public class CartControllerImpl implements CartController {
    @Autowired
    CartService cartService;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    AddressService addressService;
    @Autowired
    OrderService orderService;
    @Autowired
    SettingService settingService;
    @Override
    public ModelAndView viewCart() {
        ModelAndView view = new ModelAndView("cart-pages/index");
        cartService.activeAll(true);
        CartDTO cartDTO = cartService.getCart();
        Boolean isSelectAll = cartDTO.getCategories().stream().filter(cartCategoryDTO -> !cartCategoryDTO.getActive()).collect(Collectors.toList()).size()<=0;
        view.addObject("cart",cartDTO);
        view.addObject("addressList",addressService.findAllAddressDTOById());
        view.addAllObjects(cartService.summaryCart(cartDTO));
        view.addObject("methods",orderService.getMethod());
        view.addObject("isSelectAll",isSelectAll);
        view.addObject("priceSetting",settingService.getPriceSetting());
        return view;
    }
}

